package manfred.data.infrastructure.map;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.shared.PositiveInt;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MapPrototype {
    String name;
    MapMatrix<TilePrototype> map;
    Map<Coordinate, PersonPrototype> persons;
    Map<Coordinate, TransporterDto> portals;
    Map<Coordinate, TransporterDto> doors;
    List<EnemyPrototype> enemies;

    public MapPrototype(String name, MapMatrix<TilePrototype> map, List<PersonPrototype> persons, List<TransporterDto> portals, List<TransporterDto> doors, List<EnemyPrototype> enemies) {
        this.name = name;
        this.map = map;
        this.persons = toMapByCoordinate(persons);
        this.portals = toMapByCoordinate(portals);
        this.doors = toMapByCoordinate(doors);
        this.enemies = enemies;
    }

    private <T extends MapObject> Map<Coordinate, T> toMapByCoordinate(List<T> mapObjects) {
        return mapObjects.stream()
            .collect(Collectors.toMap(
                mapObject -> new Coordinate(mapObject.getPositionX(), mapObject.getPositionY()),
                mapObject -> mapObject
            ));
    }

    public String getName() {
        return name;
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

    public Optional<PersonPrototype> getPerson(Coordinate coordinate) {
        return Optional.ofNullable(this.persons.get(coordinate));
    }

    public Optional<TransporterDto> getPortal(Coordinate coordinate) {
        return Optional.ofNullable(this.portals.get(coordinate));
    }

    public Optional<TransporterDto> getDoor(Coordinate coordinate) {
        return Optional.ofNullable(this.doors.get(coordinate));
    }

    public List<EnemyPrototype> getEnemies() {
        return this.enemies;
    }

    public TilePrototype getFromMap(Coordinate coordinate) {
        return this.map.get(coordinate.x.value(), coordinate.y.value());
    }

    public Coordinate getBottomLeftCoordinate() {
        return new Coordinate(PositiveInt.of(0), PositiveInt.of(0));
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
