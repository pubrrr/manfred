package manfred.manfreditor.controller;

import manfred.data.InvalidInputException;
import manfred.manfreditor.map.MapProvider;

public class LoadMapCommand implements Command {

    private final MapProvider mapProviderMock;
    private final String mapName;

    public LoadMapCommand(MapProvider mapProviderMock, String mapName) {
        this.mapProviderMock = mapProviderMock;
        this.mapName = mapName;
    }

    @Override
    public CommandResult execute() {
        try {
            mapProviderMock.provide(this.mapName);
        } catch (InvalidInputException e) {
            return CommandResult.failure(e.getMessage());
        }
        return CommandResult.success();
    }
}
