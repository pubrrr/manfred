package manfred.manfreditor.controller;

import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {

    private final MapProvider mapProvider;
    private final MapModel mapModel;

    public CommandFactory(MapProvider mapProvider, MapModel mapModel) {
        this.mapProvider = mapProvider;
        this.mapModel = mapModel;
    }

    public Command createLoadMapCommand(String mapName) {
        return new LoadMapCommand(mapModel, mapProvider, mapName);
    }
}
