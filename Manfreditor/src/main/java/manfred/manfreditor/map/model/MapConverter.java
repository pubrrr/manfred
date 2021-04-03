package manfred.manfreditor.map.model;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.map.model.mapobject.MapObject;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapConverter implements ObjectConverter<MapPrototype, Map> {

    private final TileConversionRule<MapObject> tileConversionRule;

    public MapConverter(TileConversionRule<MapObject> tileConversionRule) {
        this.tileConversionRule = tileConversionRule;
    }

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
        return tileConversionRule.applicableTo(mapPrototype, coordinate)
            .orElse(MapObject::none)
            .create();
    }
}
