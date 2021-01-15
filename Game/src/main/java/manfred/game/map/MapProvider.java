package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.data.map.MapReader;

public class MapProvider {

    private final MapConverter mapConverter;
    private final MapReader mapReader;

    public MapProvider(MapConverter mapConverter, MapReader mapReader) {
        this.mapConverter = mapConverter;
        this.mapReader = mapReader;
    }

    public Map provide(String name) throws InvalidInputException {
        return mapConverter.convert(mapReader.read(name));
    }
}
