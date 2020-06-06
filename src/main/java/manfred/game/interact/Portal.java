package manfred.game.interact;

import manfred.game.controls.KeyControls;

import java.util.function.Consumer;

public class Portal extends LoadMapWorker implements Interactable {
    public Portal(String targetName, int targetSpawnX, int targetSpawnY) {
        super(targetName, targetSpawnX, targetSpawnY);
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public Consumer<KeyControls> onStep() {
        return this::triggerLoadMapInWorkerThread;
    }

    @Override
    public Consumer<KeyControls> interact() {
        return null;
    }
}
