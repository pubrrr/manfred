package manfred.game.interact;

import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.map.MapTile;

import java.awt.*;

public class Portal implements MapTile {
    private final String targetName;
    private final PositiveInt targetSpawnX;
    private final PositiveInt targetSpawnY;

    public Portal(String targetName, PositiveInt targetSpawnX, PositiveInt targetSpawnY) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> onStep() {
        return controllerInterface -> {
            controllerInterface.stop();
            return ControllerInterface.sleepWhileWorkingOn(
                new LoadMapWorker(this.targetName, this.targetSpawnX, this.targetSpawnY, controllerInterface)
            );
        };
    }

    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> interact() {
        return ControllerStateMapper::preserveState;
    }

    @Override
    public void paint(Graphics g, Integer x, Integer y) {
        // do nothing
    }
}
