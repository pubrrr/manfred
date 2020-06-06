package manfred.game.interact;

import manfred.game.controls.KeyControls;

import javax.swing.*;

abstract public class LoadMapWorker extends SwingWorker<Void, Void> {
    private String targetName;
    private int targetSpawnX;
    private int targetSpawnY;

    private KeyControls keyControls;

    public LoadMapWorker(String targetName, int targetSpawnX, int targetSpawnY) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
    }

    @Override
    protected Void doInBackground() {
        keyControls.getGamePanel().fadeOut();
        keyControls.loadMap(this.targetName);
        keyControls.resetManfredPositionTo(this.targetSpawnX, this.targetSpawnY);
        keyControls.getGamePanel().fadeIn();
        keyControls.controlManfred();
        return null;
    }

    public void triggerLoadMapInWorkerThread(KeyControls keyControls) {
        keyControls.turnOffControls();
        this.keyControls = keyControls;
        super.execute();
    }
}
