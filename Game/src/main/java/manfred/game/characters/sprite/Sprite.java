package manfred.game.characters.sprite;

import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

public interface Sprite {

    PositiveInt getHeight();

    PositiveInt getWidth();

    BufferedImage getImage();
}
