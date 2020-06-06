package manfred.game.interact;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

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

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    @Nullable
    public Consumer<KeyControls> onStep() {
        return null;
    }
}
