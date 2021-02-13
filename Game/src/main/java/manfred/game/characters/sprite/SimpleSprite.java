package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

public class SimpleSprite implements Sprite {
    private final PositiveInt width;
    private final PositiveInt height;
    private final BufferedImage image;

    public SimpleSprite(PositiveInt width, PositiveInt height, BufferedImage image) {
        this.width = width;
        this.height = height;
        this.image = image;
    }

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
