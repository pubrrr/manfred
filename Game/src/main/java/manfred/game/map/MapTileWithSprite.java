package manfred.game.map;

import manfred.game.characters.sprite.Sprite;
import manfred.game.characters.sprite.SpritePainter;

public class MapTileWithSprite extends SpritePainter<Sprite> implements MapTile {

    private final MapTile baseMapTile;

    public MapTileWithSprite(MapTile baseMapTile, Sprite sprite) {
        super(sprite);
        this.baseMapTile = baseMapTile;
    }

    @Override
    public boolean isAccessible() {
        return baseMapTile.isAccessible();
    }
}
