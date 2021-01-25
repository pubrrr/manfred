package manfred.data.infrastructure.map.tile;

import java.util.Optional;

public interface TilePrototype {

    boolean isAccessible();

    Optional<ValidatedMapTileDto> getTileObject();

    static TilePrototype notAccessible() {
        return new NonAccessiblePrototype();
    }

    static TilePrototype accessible() {
        return new AccessiblePrototype();
    }

    static ObjectPrototype.Builder withObject(ValidatedMapTileDto tileObject) {
        return new ObjectPrototype.Builder(tileObject);
    }
}
