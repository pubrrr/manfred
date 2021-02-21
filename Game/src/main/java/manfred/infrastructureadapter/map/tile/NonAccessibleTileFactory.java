package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.map.NotAccessible;

import java.util.Optional;

public class NonAccessibleTileFactory implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        TilePrototype tilePrototype = input.getFromMap(coordinate);
        return !tilePrototype.isAccessible()
            ? Optional.of(NotAccessible::new)
            : Optional.empty();
    }
}
