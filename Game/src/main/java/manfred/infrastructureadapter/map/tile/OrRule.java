package manfred.infrastructureadapter.map.tile;

import manfred.data.infrastructure.map.MapPrototype;

import java.util.Optional;

public class OrRule implements TileConversionRule {
    private final TileConversionRule wrapped;
    private final TileConversionRule or;

    public OrRule(TileConversionRule wrapped, TileConversionRule or) {
        this.wrapped = wrapped;
        this.or = or;
    }

    @Override
    public Optional<TileConversionAction> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return wrapped.applicableTo(input, coordinate)
            .or(() -> or.applicableTo(input, coordinate));
    }
}
