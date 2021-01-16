package manfred.data.map.tile;

import java.util.Optional;

public class NonAccessiblePrototype implements TilePrototype {
    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public Optional<ValidatedMapTileDto> getTileObject() {
        return Optional.empty();
    }
}
