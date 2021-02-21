package manfred.data.infrastructure.map;

@FunctionalInterface
public interface TileConversionAction<T> {
    T create();
}
