package manfred.data.infrastructure.map;

import java.util.Optional;

public class OrRule<T> implements TileConversionRule<T> {
    private final TileConversionRule<T> wrapped;
    private final TileConversionRule<T> or;

    public OrRule(TileConversionRule<T> wrapped, TileConversionRule<T> or) {
        this.wrapped = wrapped;
        this.or = or;
    }

    @Override
    public Optional<TileConversionAction<T>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return wrapped.applicableTo(input, coordinate)
            .or(() -> or.applicableTo(input, coordinate));
    }
}
