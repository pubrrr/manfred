package manfred.game.interact;

import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;

import java.awt.*;
import java.util.function.Consumer;

public class Door extends LoadMapWorker implements Interactable {
    private final GameConfig gameConfig;

    public Door(String targetName, int targetSpawnX, int targetSpawnY, GameConfig gameConfig) {
        super(targetName, targetSpawnX, targetSpawnY);
        this.gameConfig = gameConfig;
    }

    @Override
    public Consumer<KeyControls> interact() {
        return this::triggerLoadMapInWorkerThread;
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.setColor(Color.YELLOW);
        g.fillRect(
                gameConfig.getPixelBlockSize() * x - offset.x,
                gameConfig.getPixelBlockSize() * y - offset.y,
                gameConfig.getPixelBlockSize(),
                gameConfig.getPixelBlockSize()
        );
    }
}
