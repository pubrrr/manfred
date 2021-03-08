package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

public class SimpleSprite extends Sprite {
    private final PositiveInt width;
    private final PositiveInt height;
    private final int yOffset;
    private final BufferedImage image;

    public SimpleSprite(PositiveInt width, PositiveInt height, int yOffset, BufferedImage image) {
        this.width = width;
        this.height = height;
        this.yOffset = yOffset;
        this.image = image;
    }

    public SimpleSprite(PositiveInt width, PositiveInt height, BufferedImage image) {
        this(width, height, 0, image);
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
    public int getYOffset() {
        return this.yOffset;
    }

    @Override
    public BufferedImage getImage() {
        return this.image;
    }
}
