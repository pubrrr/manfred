package manfred.game.interact;

import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.MapTile;

import java.awt.*;

public class Door implements MapTile {
    private final String targetName;
    private final PositiveInt targetSpawnX;
    private final PositiveInt targetSpawnY;

    public Door(String targetName, PositiveInt targetSpawnX, PositiveInt targetSpawnY) {
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
    public void paint(Graphics g, PanelCoordinate coordinate) {
        // do nothing
    }
}
