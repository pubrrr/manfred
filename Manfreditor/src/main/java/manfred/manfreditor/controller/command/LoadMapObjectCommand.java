package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.springframework.stereotype.Component;

public class LoadMapObjectCommand implements Command {

    private final MapTileReader mapTileReader;
    private final MapObjectRepository mapObjectRepository;
    private final String tileName;

    public LoadMapObjectCommand(MapTileReader mapTileReader, MapObjectRepository mapObjectRepository, String tileName) {
        this.mapTileReader = mapTileReader;
        this.mapObjectRepository = mapObjectRepository;
        this.tileName = tileName;
    }

    @Override
    public CommandResult execute() {
        ValidatedMapTileDto validatedMapTileDto;
        try {
            validatedMapTileDto = mapTileReader.load(this.tileName);
        } catch (InvalidInputException e) {
            return CommandResult.failure(e.getMessage());
        }
        MapObjectRepository.ObjectKey newKey = mapObjectRepository.populateWith(validatedMapTileDto);
        return CommandResult.success(() -> mapObjectRepository.delete(newKey));
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
            return new LoadMapObjectCommand(mapTileReader, mapObjectRepository, tileName);
        }
    }
}
