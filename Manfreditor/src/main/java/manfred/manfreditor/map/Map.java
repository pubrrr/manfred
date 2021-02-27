package manfred.manfreditor.map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.mapobject.MapObject;

import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Map {

    private final String name;
    private final java.util.Map<TileCoordinate, MapObject> mapMatrix;
    private final PositiveInt sizeX;
    private final PositiveInt sizeY;

    public Map(String name, java.util.Map<MapPrototype.Coordinate, MapObject> mapMatrix) {
        this.name = name;
        this.mapMatrix = mapMatrix.entrySet().stream()
            .collect(Collectors.toMap(
                mapObjectByCoordinatePrototype -> new TileCoordinate(mapObjectByCoordinatePrototype.getKey()),
                java.util.Map.Entry::getValue
            ));

        if (mapMatrix.isEmpty()) {
            this.sizeX = PositiveInt.of(0);
            this.sizeY = PositiveInt.of(0);
        } else {
            this.sizeX = findMaxValue(mapMatrix.keySet(), MapPrototype.Coordinate::getX).add(1);
            this.sizeY = findMaxValue(mapMatrix.keySet(), MapPrototype.Coordinate::getY).add(1);
        }
    }

    public PositiveInt getSizeX() {
        return sizeX;
    }

    public PositiveInt getSizeY() {
        return sizeY;
    }

    private PositiveInt findMaxValue(Set<MapPrototype.Coordinate> coordinates, Function<MapPrototype.Coordinate, PositiveInt> coordinateSelector) {
        return PositiveInt.of(coordinates.stream()
            .map(coordinateSelector)
            .map(PositiveInt::value)
            .reduce(0, Math::max));
    }

    public String getName() {
        return name;
    }

    public TileCoordinate tileCoordinate(PositiveInt x, PositiveInt y) {
        return new TileCoordinate(x, y);
    }

    public MapObject getObjectAt(TileCoordinate tileCoordinate) {
        // TODO null case!
        return this.mapMatrix.get(tileCoordinate);
    }

    public SortedMap<TileCoordinate, MapObject> getObjects() {
        return new TreeMap<>(mapMatrix);
    }

    @EqualsAndHashCode
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    @Getter
    public static class TileCoordinate implements Comparable<TileCoordinate> {
        PositiveInt x;
        PositiveInt y;

        private TileCoordinate(PositiveInt x, PositiveInt y) {
            this.x = x;
            this.y = y;
        }

        private TileCoordinate(MapPrototype.Coordinate coordinatePrototype) {
            this.x = coordinatePrototype.getX();
            this.y = coordinatePrototype.getY();
        }

        @Override
        public int compareTo(TileCoordinate other) {
            // lower y means further down, i.e. should be painted later to cover the further up object
            int result = Integer.compare(other.y.value(), this.y.value());
            if (result == 0) {
                result = Integer.compare(this.x.value(), other.x.value());
            }
            return result;
        }
    }
}
