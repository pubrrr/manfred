package manfred.game.interact;

import manfred.game.config.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;

import java.awt.*;
import java.util.function.Function;

public class Door implements Interactable {
    private final String targetName;
    private final int targetSpawnX;
    private final int targetSpawnY;
    private final GameConfig gameConfig;

    public Door(String targetName, int targetSpawnX, int targetSpawnY, GameConfig gameConfig) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
        this.gameConfig = gameConfig;
    }

    @Override
    public Function<ManfredController, ControllerInterface> interact() {
        return controllerInterface -> {
            controllerInterface.stop();
            return ControllerInterface.sleepWhileWorkingOn(new LoadMapWorker(this.targetName, this.targetSpawnX, this.targetSpawnY, controllerInterface));
        };
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
