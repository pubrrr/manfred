package manfred.game.map;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

public class Accessible implements MapTile {
    private static Accessible instance;

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    @Nullable
    public Consumer<KeyControls> onStep() {
        return null;
    }

    public static Accessible getInstance() {
        if (instance == null) {
            instance = new Accessible();
        }
        return instance;
    }
}
