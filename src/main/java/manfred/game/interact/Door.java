package manfred.game.interact;

import manfred.game.controls.KeyControls;

import javax.swing.*;
import java.util.function.Consumer;

public class Door extends SwingWorker<Void, Void> implements Interactable {
    private String targetName;
    private int targetSpawnX;
    private int targetSpawnY;

    private KeyControls keyControls;

    public Door(String targetName, int targetSpawnX, int targetSpawnY) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
    }

    @Override
    public Consumer<KeyControls> interact() {
        return this::triggerLoadMapInWorkerThread;
    }

    @Override
    protected Void doInBackground() {
        keyControls.turnOffControls();
        keyControls.getGamePanel().fadeOut();
        keyControls.loadMap(this.targetName);
        keyControls.resetManfredPositionTo(this.targetSpawnX, this.targetSpawnY);
        keyControls.getGamePanel().fadeIn();
        keyControls.controlManfred();
        return null;
    }

    public void triggerLoadMapInWorkerThread(KeyControls keyControls) {
        this.keyControls = keyControls;
        super.execute();
    }
}
