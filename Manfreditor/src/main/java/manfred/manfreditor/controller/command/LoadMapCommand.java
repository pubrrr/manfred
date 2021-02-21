package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;
import org.springframework.stereotype.Component;

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

    @Component
    public static class Factory {
        private final MapProvider mapProvider;
        private final MapModel mapModel;

        public Factory(MapProvider mapProvider, MapModel mapModel) {
            this.mapProvider = mapProvider;
            this.mapModel = mapModel;
        }

        public Command create(String mapName) {
            return new LoadMapCommand(mapModel, mapProvider, mapName);
        }
    }
}
