package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.map.MapTile;
import manfred.game.map.NotAccessible;

import java.util.Optional;

public class NonAccessibleTileFactory implements TileConversionRule<MapTile> {

    @Override
    public Optional<TileConversionAction<MapTile>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        TilePrototype tilePrototype = input.getFromMap(coordinate);
        return !tilePrototype.isAccessible()
            ? Optional.of(NotAccessible::new)
            : Optional.empty();
    }
}
