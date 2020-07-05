package manfred.game.map;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.Paintable;
import org.springframework.lang.Nullable;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public interface MapTile extends Paintable {
    boolean isAccessible();

    @Nullable
    Consumer<KeyControls> onStep();

    @Nullable
    default BufferedImage getImage() {
        return null;
    }
}
