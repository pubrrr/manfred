package manfred.game;

import manfred.game.attack.Attack;
import manfred.game.attack.AttacksContainer;
import manfred.game.characters.Manfred;
import manfred.game.controls.KeyControls;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.graphics.ManfredWindow;

import java.util.function.Consumer;

public class GameRunner implements Runnable {
    final static int REPAINT_PERIOD = 15;

    private final KeyControls keyControls;
    private final ManfredWindow window;
    private final Manfred manfred;
    private final EnemiesWrapper enemiesWrapper;
    private final AttacksContainer attacksContainer;

    public GameRunner(KeyControls keyControls, ManfredWindow window, Manfred manfred, EnemiesWrapper enemiesWrapper, AttacksContainer attacksContainer) {
        this.keyControls = keyControls;
        this.window = window;
        this.manfred = manfred;
        this.enemiesWrapper = enemiesWrapper;
        this.attacksContainer = attacksContainer;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long beforeTime = System.currentTimeMillis();
            move();
            window.repaint();

            long timeNeeded = System.currentTimeMillis() - beforeTime;
            try {
                Thread.sleep(
                        REPAINT_PERIOD - timeNeeded > 0
                                ? REPAINT_PERIOD - timeNeeded
                                : 1
                );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void move() {
        Consumer<KeyControls> callback = this.manfred.move();
        if (callback != null) {
            callback.accept(this.keyControls);
        }

        enemiesWrapper.forEach(enemy -> enemy.move(manfred));
        attacksContainer.forEach(Attack::move);

        enemiesWrapper.forEach(
                enemy -> attacksContainer.forEach(
                        attack -> attack.checkHit(enemy)
                )
        );
        enemiesWrapper.removeKilled();
        attacksContainer.removeResolved();
    }
}