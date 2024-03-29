package manfred.manfreditor.map.model;

import manfred.data.ObjectProvider;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.MapReader;
import manfred.data.persistence.reader.MapSource;
import org.springframework.stereotype.Component;

@Component
public class MapProvider extends ObjectProvider<MapSource, MapPrototype, Map> {

    public MapProvider(MapReader mapReader, MapConverter mapConverter) {
        super(mapReader, mapConverter);
    }
}
