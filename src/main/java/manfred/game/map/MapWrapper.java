package manfred.game.map;

import manfred.game.exception.InvalidInputException;

import java.io.IOException;

public class MapWrapper {
    private MapReader mapReader;
    private Map map;

    public MapWrapper(MapReader mapReader, Map initialMap) {
        this.mapReader = mapReader;
        this.map = initialMap;
    }

    public Map getMap() {
        return map;
    }

    public void loadMap(String name) throws InvalidInputException, IOException {
        this.map = mapReader.load(name);
    }
}
