package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;
import manfred.game.graphics.PanelCoordinate;

import java.awt.image.BufferedImage;

public abstract class Sprite {

    abstract public PositiveInt getHeight();

    abstract public PositiveInt getWidth();

    abstract protected BufferedImage getImage();

    public final LocatedSprite at(PanelCoordinate bottomLeftCoordinate) {
        return new LocatedSprite(bottomLeftCoordinate, getWidth(), getHeight(), getImage());
    }
}
