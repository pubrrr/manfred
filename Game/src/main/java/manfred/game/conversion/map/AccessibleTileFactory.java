package manfred.game.conversion.map;

import manfred.data.infrastructure.map.ValidatedMapDto;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.map.Accessible;

import java.util.Optional;

public class AccessibleTileFactory implements TileConversionRule {

    @Override
    public Optional<TileConversionAction> applicableTo(ValidatedMapDto input, int x, int y) {
        TilePrototype tilePrototype = input.getMap().get(x, y);
        return tilePrototype.isAccessible()
            ? Optional.of(Accessible::getInstance)
            : Optional.empty();
    }
}
