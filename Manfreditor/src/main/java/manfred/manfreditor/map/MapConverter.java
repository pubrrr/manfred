package manfred.manfreditor.map;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.manfreditor.map.object.MapObject;

import java.util.stream.Collectors;

public class MapConverter implements ObjectConverter<MapPrototype, Map> {

    @Override
    public Map convert(MapPrototype mapPrototype) {
        java.util.Map<MapPrototype.Coordinate, MapObject> mapMatrix = mapPrototype.getCoordinateSet().stream()
            .collect(Collectors.toMap(
                coordinate -> coordinate,
                coordinate -> createMapObject(mapPrototype, coordinate)
            ));

        return new Map(mapPrototype.getName(), mapMatrix);
    }

    private MapObject createMapObject(MapPrototype mapPrototype, MapPrototype.Coordinate coordinate) {
        return MapObject.none();
    }
}
