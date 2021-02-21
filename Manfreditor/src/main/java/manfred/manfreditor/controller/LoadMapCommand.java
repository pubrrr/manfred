package manfred.manfreditor.controller;

import manfred.data.InvalidInputException;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;

public class LoadMapCommand implements Command {

    private final MapModel mapModel;
    private final MapProvider mapProvider;
    private final String mapName;

    public LoadMapCommand(MapModel mapModel, MapProvider mapProvider, String mapName) {
        this.mapModel = mapModel;
        this.mapProvider = mapProvider;
        this.mapName = mapName;
    }

    @Override
    public CommandResult execute() {
        try {
            Map newMap = mapProvider.provide(this.mapName);
            mapModel.setMap(newMap);
        } catch (InvalidInputException e) {
            return CommandResult.failure(e.getMessage());
        }
        return CommandResult.success();
    }
}
