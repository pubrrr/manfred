package manfred.game.interact;

import manfred.data.shared.PositiveInt;
import manfred.game.controls.ManfredController;

import javax.swing.*;

public class LoadMapWorker extends SwingWorker<ManfredController, Void> {
    private final String targetName;
    private final PositiveInt targetSpawnX;
    private final PositiveInt targetSpawnY;
    private final ManfredController sleepingController;

    public LoadMapWorker(String targetName, PositiveInt targetSpawnX, PositiveInt targetSpawnY, ManfredController sleepingController) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
        this.sleepingController = sleepingController;
    }

    @Override
    protected ManfredController doInBackground() {
        sleepingController.getGamePanel().fadeOut();
        sleepingController.loadMap(this.targetName);
        sleepingController.resetManfredPositionTo(this.targetSpawnX.value(), this.targetSpawnY.value());
        sleepingController.getGamePanel().fadeIn();
        return sleepingController;
    }
}
