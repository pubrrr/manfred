package manfred.data.shared;

import java.awt.image.BufferedImage;

public interface SpriteInterface {

    PositiveInt getHeight();

    PositiveInt getWidth();

    default int getYOffset() {
        return 0;
    }

    BufferedImage getImage();
}
