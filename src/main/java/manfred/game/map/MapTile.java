package manfred.game.map;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public interface MapTile {
    boolean isAccessible();

    @Nullable
    Consumer<KeyControls> onStep();

    @Nullable
    default BufferedImage getImage() {
        return null;
    }
}
