package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;

public class ConcreteMapObject implements MapObject {

    private final String name;
    private final MapMatrix<TilePrototype> structure; // TODO probably change type of this
    private final Sprite sprite;

    public ConcreteMapObject(String name, MapMatrix<TilePrototype> structure, Sprite sprite) {
        this.name = name;
        this.structure = structure;
        this.sprite = sprite;
    }

    public String getName() {
        return this.name;
    }
}
