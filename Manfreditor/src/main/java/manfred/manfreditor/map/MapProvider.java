package manfred.manfreditor.map;

import manfred.data.ObjectProvider;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.MapReader;

public class MapProvider extends ObjectProvider<MapPrototype, Map> {

    public MapProvider(MapReader mapReader, MapConverter objectConverter) {
        super(mapReader, objectConverter);
    }
}
