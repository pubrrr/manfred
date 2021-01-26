package manfred.game.interact;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;

import java.awt.*;

public class Door implements Interactable {
    private final String targetName;
    private final int targetSpawnX;
    private final int targetSpawnY;

    public Door(String targetName, int targetSpawnX, int targetSpawnY) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
    }

    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> interact() {
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
        // do nothing
    }
}
