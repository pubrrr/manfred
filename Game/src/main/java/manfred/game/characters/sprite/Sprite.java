package manfred.game.characters.sprite;

import manfred.data.shared.SpriteInterface;
import manfred.game.graphics.PanelCoordinate;

public abstract class Sprite implements SpriteInterface {

    public final LocatedSprite at(PanelCoordinate bottomLeftCoordinate) {
        return new LocatedSprite(bottomLeftCoordinate, getWidth(), getHeight(), getImage());
    }
}
