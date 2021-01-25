package manfred.data.infrastructure.map.tile;

import java.util.Optional;

public class AccessiblePrototype implements TilePrototype {
    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public Optional<ValidatedMapTileDto> getTileObject() {
        return Optional.empty();
    }
}
