package manfred.manfreditor.mapobject;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.data.shared.SpriteInterface;

import java.awt.image.BufferedImage;

@AllArgsConstructor
public class Sprite implements SpriteInterface {

    private final PositiveInt height;
    private final PositiveInt width;
    private final BufferedImage image;

    @Override
    public PositiveInt getHeight() {
        return this.height;
    }

    @Override
    public PositiveInt getWidth() {
        return this.width;
    }

    @Override
    public BufferedImage getImage() {
        return this.image;
    }
}
