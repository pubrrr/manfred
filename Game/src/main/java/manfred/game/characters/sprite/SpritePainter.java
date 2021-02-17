package manfred.game.characters.sprite;

import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;

public abstract class SpritePainter<SPRITE extends Sprite> {

    protected final SPRITE sprite;

    public SpritePainter(SPRITE sprite) {
        this.sprite = sprite;
    }

    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        g.drawSprite(this.sprite.at(bottomLeftCoordinate));
    }
}
