package manfred.manfreditor.map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.object.MapObject;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Map {

    private final String name;
    private final java.util.Map<TileCoordinate, MapObject> mapMatrix;
    private final int sizeX;
    private final int sizeY;

    public Map(String name, java.util.Map<MapPrototype.Coordinate, MapObject> mapMatrix) {
        this.name = name;
        this.mapMatrix = mapMatrix.entrySet().stream()
            .collect(Collectors.toMap(
                mapObjectByCoordinatePrototype -> new TileCoordinate(mapObjectByCoordinatePrototype.getKey()),
                java.util.Map.Entry::getValue
            ));

        if (mapMatrix.isEmpty()) {
            this.sizeX = 0;
            this.sizeY = 0;
        } else {
            this.sizeX = findMaxValue(mapMatrix.keySet(), MapPrototype.Coordinate::getX) + 1;
            this.sizeY = findMaxValue(mapMatrix.keySet(), MapPrototype.Coordinate::getY) + 1;
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    private Integer findMaxValue(Set<MapPrototype.Coordinate> coordinates, Function<MapPrototype.Coordinate, PositiveInt> coordinateMapper) {
        return coordinates.stream()
            .map(coordinateMapper)
            .map(PositiveInt::value)
            .reduce(0, Math::max);
    }

    public String getName() {
        return name;
    }

    @EqualsAndHashCode
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    @Getter
    public static class TileCoordinate {
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
    }
}
