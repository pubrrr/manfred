package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.springframework.stereotype.Component;

public class LoadMapTileCommand implements Command {

    private final MapTileReader mapTileReader;
    private final MapObjectRepository mapObjectRepository;
    private final String tileName;

    public LoadMapTileCommand(MapTileReader mapTileReader, MapObjectRepository mapObjectRepository, String tileName) {
        this.mapTileReader = mapTileReader;
        this.mapObjectRepository = mapObjectRepository;
        this.tileName = tileName;
    }

    @Override
    public CommandResult execute() {
        try {
            ValidatedMapTileDto validatedMapTileDto = mapTileReader.load(this.tileName);
            mapObjectRepository.populateWith(validatedMapTileDto);
        } catch (InvalidInputException e) {
            return CommandResult.failure(e.getMessage());
        }
        return CommandResult.success();
    }

    @Component
    public static class Factory {

        private final MapTileReader mapTileReader;
        private final MapObjectRepository mapObjectRepository;

        public Factory(MapTileReader mapTileReader, MapObjectRepository mapObjectRepository) {
            this.mapTileReader = mapTileReader;
            this.mapObjectRepository = mapObjectRepository;
        }

        public Command create(String tileName) {
            return new LoadMapTileCommand(mapTileReader, mapObjectRepository, tileName);
        }
    }
}
