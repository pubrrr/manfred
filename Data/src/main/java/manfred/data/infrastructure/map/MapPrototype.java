package manfred.data.infrastructure.map;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.person.PersonPrototype;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.persistence.reader.MapSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MapPrototype extends MapStructurePrototype {

    String name;
    Map<Coordinate, PersonPrototype> persons;
    Map<Coordinate, TransporterDto> portals;
    Map<Coordinate, TransporterDto> doors;
    List<EnemyPrototype> enemies;
    MapSource mapSource;

    public MapPrototype(String name, MapMatrix<TilePrototype> map, List<PersonPrototype> persons, List<TransporterDto> portals, List<TransporterDto> doors, List<EnemyPrototype> enemies, MapSource mapSource) {
        super(map);
        this.name = name;
        this.persons = toMapByCoordinate(persons);
        this.portals = toMapByCoordinate(portals);
        this.doors = toMapByCoordinate(doors);
        this.enemies = enemies;
        this.mapSource = mapSource;
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

    public MapSource getMapSource() {
        return mapSource;
    }
}
