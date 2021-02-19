package manfred.manfreditor.map;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.map.MapPrototype;

public class MapConverter implements ObjectConverter<MapPrototype, Map> {

    @Override
    public Map convert(MapPrototype mapPrototype) {
        return new Map(mapPrototype.getName());
    }
}
