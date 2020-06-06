package manfred.game.map;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

public class NotAccessible implements MapTile {
    private static NotAccessible instance;

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    @Nullable
    public Consumer<KeyControls> onStep() {
        return null;
    }

    public static NotAccessible getInstance() {
        if (instance == null) {
            instance = new NotAccessible();
        }
        return instance;
    }
}
