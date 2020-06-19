package manfred.game.map;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class NotAccessible implements MapTile {
    private BufferedImage tileImage;

    public NotAccessible(BufferedImage tileImage) {

        this.tileImage = tileImage;
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

    @Override
    public BufferedImage getImage() {
        return tileImage;
    }
}
