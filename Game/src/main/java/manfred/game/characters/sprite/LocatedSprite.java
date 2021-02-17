package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;
import manfred.game.geometry.Rectangle;
import manfred.game.graphics.PanelCoordinate;

import java.awt.image.BufferedImage;

public class LocatedSprite extends Rectangle<PanelCoordinate> {

    private final PositiveInt width;
    private final PositiveInt height;
    private final BufferedImage image;

    public LocatedSprite(PanelCoordinate bottomLeft, PositiveInt width, PositiveInt height, BufferedImage image) {
        super(bottomLeft, width, height);
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public PositiveInt getWidth() {
        return width;
    }

    public PositiveInt getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }
}
