package manfred.manfreditor.controller;

import manfred.manfreditor.map.MapProvider;

public class CommandFactory {

    private final MapProvider mapProviderMock;

    public CommandFactory(MapProvider mapProviderMock) {
        this.mapProviderMock = mapProviderMock;
    }

    public Command createLoadMapCommand(String mapName) {
        return new LoadMapCommand(mapProviderMock, mapName);
    }
}
