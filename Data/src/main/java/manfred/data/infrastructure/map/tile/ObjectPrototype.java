package manfred.data.infrastructure.map.tile;

import java.util.Optional;

public class ObjectPrototype implements TilePrototype {

    private final TilePrototype baseTile;
    private final ValidatedMapTileDto tileObject;

    private ObjectPrototype(TilePrototype baseTile, ValidatedMapTileDto tileObject) {
        this.baseTile = baseTile;
        this.tileObject = tileObject;
    }

    @Override
    public boolean isAccessible() {
        return baseTile.isAccessible();
    }

    @Override
    public Optional<ValidatedMapTileDto> getTileObject() {
        return Optional.of(tileObject);
    }

    public static class Builder {

        private final ValidatedMapTileDto tileObject;

        public Builder(ValidatedMapTileDto tileObject) {
            this.tileObject = tileObject;
        }

        public TilePrototype andAccessible() {
            return new ObjectPrototype(TilePrototype.accessible(), this.tileObject);
        }

        public TilePrototype andNotAccessible() {
            return new ObjectPrototype(TilePrototype.notAccessible(), this.tileObject);
        }
    }
}
