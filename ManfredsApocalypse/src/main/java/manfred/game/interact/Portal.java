package manfred.game.interact;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;

import java.awt.*;
import java.util.function.Function;

public class Portal implements Interactable {
    private final String targetName;
    private final int targetSpawnX;
    private final int targetSpawnY;

    public Portal(String targetName, int targetSpawnX, int targetSpawnY) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public Function<ManfredController, ControllerInterface> onStep() {
        return controllerInterface -> {
            controllerInterface.stop();
            return ControllerInterface.sleepWhileWorkingOn(
                new LoadMapWorker(this.targetName, this.targetSpawnX, this.targetSpawnY, controllerInterface)
            );
        };
    }

    @Override
    public Function<ManfredController, ControllerInterface> interact() {
        return ControllerInterface::self;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        // do nothing
    }
}
