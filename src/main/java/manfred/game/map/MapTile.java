package manfred.game.map;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

public interface MapTile {
    boolean isAccessible();

    @Nullable
    Consumer<KeyControls> onStep();
}
