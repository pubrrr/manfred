package manfred.manfreditor.controller.command;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.persistence.reader.MapTileSource;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.springframework.stereotype.Component;

import java.io.File;

@AllArgsConstructor
public class LoadMapObjectCommand implements Command {

    private final MapTileReader mapTileReader;
    private final MapObjectRepository mapObjectRepository;
    private final File yamlFile;
    private final File imageFile;

    @Override
    public CommandResult execute() {
        if (!yamlFile.isFile() || !yamlFile.getName().endsWith("yaml")) {
            return CommandResult.failure(yamlFile + " is not a yaml file");
        }
        if (!imageFile.isFile() || !imageFile.getName().endsWith("png")) {
            return CommandResult.failure(imageFile + " is not a png file");
        }

        ValidatedMapTileDto validatedMapTileDto;
        try {
            validatedMapTileDto = mapTileReader.load(new MapTileSource(yamlFile, imageFile));
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

        public Command create(String yamlFilePath, String imageFilePath) {
            return new LoadMapObjectCommand(mapTileReader, mapObjectRepository, new File(yamlFilePath), new File(imageFilePath));
        }

        public Command create(File yamlFile, File imageFile) {
            return new LoadMapObjectCommand(mapTileReader, mapObjectRepository, yamlFile, imageFile);
        }
    }
}
