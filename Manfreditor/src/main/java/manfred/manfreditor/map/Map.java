package manfred.manfreditor.map;

import io.vavr.Function1;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.gui.view.GridCoordinate;
import manfred.manfreditor.mapobject.MapObject;

import java.util.Set;
import java.util.function.Function;

public class Map {

    private final String name;
    private final io.vavr.collection.Map<TileCoordinate, MapObject> mapMatrix;
    private final PositiveInt sizeX;
    private final PositiveInt sizeY;

    public Map(String name, java.util.Map<MapPrototype.Coordinate, MapObject> mapMatrix) {
        this.name = name;
        this.mapMatrix = HashMap.ofAll(
            mapMatrix.entrySet().stream(),
            mapObjectByCoordinatePrototype -> new TileCoordinate(mapObjectByCoordinatePrototype.getKey()),
            java.util.Map.Entry::getValue
        );

        if (mapMatrix.isEmpty()) {
            this.sizeX = PositiveInt.of(0);
            this.sizeY = PositiveInt.of(0);
        } else {
            this.sizeX = findMaxValue(mapMatrix.keySet(), MapPrototype.Coordinate::getX).add(1);
            this.sizeY = findMaxValue(mapMatrix.keySet(), MapPrototype.Coordinate::getY).add(1);
        }
    }

    public Map(String name, PositiveInt columns, PositiveInt rows) {
        this.name = name;
        this.sizeX = columns;
        this.sizeY = rows;
        List<Integer> xCoordinates = List.range(0, columns.value());
        List<Integer> yCoordinates = List.range(0, rows.value());

        this.mapMatrix = xCoordinates.crossProduct(yCoordinates)
            .map(xAndY -> new TileCoordinate(PositiveInt.of(xAndY._1), PositiveInt.of(xAndY._2)))
            .toMap(Function.identity(), Function1.constant(MapObject.none()));
    }

    private Map(String name, io.vavr.collection.Map<TileCoordinate, MapObject> mapMatrix, PositiveInt sizeX, PositiveInt sizeY) {
        this.name = name;
        this.mapMatrix = mapMatrix;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
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

    public MapObject getObjectAt(TileCoordinate tileCoordinate) {
        return this.mapMatrix.get(tileCoordinate).get();
    }

    public io.vavr.collection.Map<TileCoordinate, MapObject> getObjects() {
        return mapMatrix;
    }

    public Map insertObjectAt(MapObject mapObject, TileCoordinate tileCoordinate) {
        return new Map(this.name, this.mapMatrix.put(tileCoordinate, mapObject), this.sizeX, this.sizeY);
    }

    @EqualsAndHashCode
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public class TileCoordinate {
        PositiveInt x;
        PositiveInt y;

        protected TileCoordinate(PositiveInt x, PositiveInt y) {
            this.x = x;
            this.y = y;
        }

        public TileCoordinate translateBy(MapPrototype.Coordinate coordinatePrototype) {
            return new TileCoordinate(this.x.add(coordinatePrototype.getX()), this.y.add(coordinatePrototype.getY()));
        }

        public TileCoordinate offsetBy(MapPrototype.Coordinate originCoordinate) {
            return new TileCoordinate(
                PositiveInt.of(this.x.value() - originCoordinate.getX().value()),
                PositiveInt.of(this.y.value() - originCoordinate.getY().value())
            );
        }

        private TileCoordinate(MapPrototype.Coordinate coordinatePrototype) {
            this.x = coordinatePrototype.getX();
            this.y = coordinatePrototype.getY();
        }

        public TileCoordinateWithInvertedY invertY() {
            return new TileCoordinateWithInvertedY(this.x, PositiveInt.of(getSizeY().value() - this.y.value() - 1));
        }

        public String shortRepresentation() {
            return "(" + x.value() + "," + y.value() + ")";
        }
    }

    @EqualsAndHashCode
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    @Getter
    @ToString
    public static class TileCoordinateWithInvertedY implements GridCoordinate {
        PositiveInt x;
        PositiveInt y;

        protected TileCoordinateWithInvertedY(PositiveInt x, PositiveInt y) {
            this.x = x;
            this.y = y;
        }
    }
}
