package manfred.game.conversion.map;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.map.NotAccessible;

import java.util.Optional;

public class NonAccessibleTileFactory implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y) {
        TilePrototype tilePrototype = input.getMap().get(x, y);
        return !tilePrototype.isAccessible()
            ? Optional.of(NotAccessible::new)
            : Optional.empty();
    }
}
