package manfred.data.infrastructure.map;

import java.util.Optional;

@FunctionalInterface
public interface TileConversionRule<T> {

    Optional<TileConversionAction<T>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate);

    default TileConversionRule<T> orElse(TileConversionRule<T> next) {
        return new OrRule<T>(this, next);
    }
}
