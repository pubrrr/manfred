package manfred.infrastructureadapter.map;

import manfred.data.ObjectProvider;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.MapReader;
import manfred.game.map.Map;
import org.springframework.stereotype.Component;

@Component
public class MapProvider extends ObjectProvider<MapPrototype, Map> {

    public MapProvider(MapReader mapReader, MapConverter mapConverter) {
        super(mapReader, mapConverter);
    }
}
