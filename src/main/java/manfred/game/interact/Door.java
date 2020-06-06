package manfred.game.interact;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

public class Door extends LoadMapWorker implements Interactable {
    public Door(String targetName, int targetSpawnX, int targetSpawnY) {
        super(targetName, targetSpawnX, targetSpawnY);
    }

    @Override
    public Consumer<KeyControls> interact() {
        return this::triggerLoadMapInWorkerThread;
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
