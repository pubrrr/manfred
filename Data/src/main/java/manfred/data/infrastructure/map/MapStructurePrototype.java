package manfred.data.infrastructure.map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.shared.PositiveInt;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MapStructurePrototype {

    private final MapMatrix<TilePrototype> map;

    protected MapStructurePrototype(MapMatrix<TilePrototype> map) {
        this.map = map;
    }

    public TilePrototype getFromMap(Coordinate coordinate) {
        return this.map.get(coordinate.x.value(), coordinate.y.value());
    }

    public Coordinate getBottomLeftCoordinate() {
        return new Coordinate(PositiveInt.of(0), PositiveInt.of(0));
    }

    public PositiveInt.Strict getSizeX() {
        return this.map.sizeX();
    }

    public List<Coordinate> getCoordinateSet() {
        return IntStream.rangeClosed(0, map.sizeX().value() - 1)
            .boxed()
            .flatMap(x -> IntStream.rangeClosed(0, map.sizeY().value() - 1).boxed().map(y -> new Coordinate(PositiveInt.of(x), PositiveInt.of(y))))
            .collect(Collectors.toList());
    }

    @EqualsAndHashCode
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    @Getter
    public static class Coordinate {
        PositiveInt x;
        PositiveInt y;

        protected Coordinate(PositiveInt x, PositiveInt y) {
            this.x = x;
            this.y = y;
        }

        public String shortRepresentation() {
            return "(" + x.value() + "," + y.value() + ")";
        }
    }
}
