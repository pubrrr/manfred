package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.map.Accessible;

import java.util.Optional;

public class AccessibleTileFactory implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, int x, int y) {
        TilePrototype tilePrototype = input.getMap().get(x, y);
        return tilePrototype.isAccessible()
            ? Optional.of(Accessible::getInstance)
            : Optional.empty();
    }
}
