package manfred.game.map;

import manfred.data.ObjectProvider;
import manfred.data.map.MapReader;
import manfred.data.map.ValidatedMapDto;
import org.springframework.stereotype.Component;

@Component
public class MapProvider extends ObjectProvider<ValidatedMapDto, Map> {

    public MapProvider(MapReader mapReader, MapConverter mapConverter) {
        super(mapReader, mapConverter);
    }
}