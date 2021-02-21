package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.map.Accessible;

import java.util.Optional;

public class AccessibleTileFactory implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        TilePrototype tilePrototype = input.getFromMap(coordinate);
        return tilePrototype.isAccessible()
            ? Optional.of(Accessible::new)
            : Optional.empty();
    }
}
