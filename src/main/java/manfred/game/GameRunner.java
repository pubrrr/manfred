package manfred.game;

import manfred.game.attack.AttacksContainer;
import manfred.game.characters.Manfred;
import manfred.game.controls.KeyControls;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.Enemy;
import manfred.game.graphics.ManfredWindow;

import java.util.function.Consumer;

public class GameRunner implements Runnable {
    final static int REPAINT_PERIOD = 30;

    private KeyControls keyControls;
    private ManfredWindow window;
    private Manfred manfred;
    private EnemiesWrapper enemiesWrapper;
    private AttacksContainer attacksContainer;

    public GameRunner(KeyControls keyControls, ManfredWindow window, Manfred manfred, EnemiesWrapper enemiesWrapper, AttacksContainer attacksContainer) {
        this.keyControls = keyControls;
        this.window = window;
        this.manfred = manfred;
        this.enemiesWrapper = enemiesWrapper;
        this.attacksContainer = attacksContainer;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            move();
            window.repaint();
            try {
                Thread.sleep(REPAINT_PERIOD);
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

        for (Enemy enemy : enemiesWrapper.getEnemies()) {
            enemy.move(manfred);
        }
    }
}