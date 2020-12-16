package manfred.game.interact;

import manfred.game.controls.ManfredController;

import javax.swing.*;

public class LoadMapWorker extends SwingWorker<ManfredController, Void> {
    private final String targetName;
    private final int targetSpawnX;
    private final int targetSpawnY;
    private final ManfredController sleepingController;

    public LoadMapWorker(String targetName, int targetSpawnX, int targetSpawnY, ManfredController sleepingController) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
        this.sleepingController = sleepingController;
    }

    @Override
    protected ManfredController doInBackground() {
        sleepingController.getGamePanel().fadeOut();
        sleepingController.loadMap(this.targetName);
        sleepingController.resetManfredPositionTo(this.targetSpawnX, this.targetSpawnY);
        sleepingController.getGamePanel().fadeIn();
        return sleepingController;
    }
}
