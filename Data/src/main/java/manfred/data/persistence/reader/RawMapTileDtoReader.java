package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedFunction1;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.RawMapTileDto;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import static io.vavr.API.Try;

@Component
@AllArgsConstructor
public class RawMapTileDtoReader {

    private final ObjectMapper objectMapper;
    private final ImageLoader imageLoader;
    private final UrlHelper urlHelper;

    public RawMapTileDto load(String name) throws InvalidInputException {
        File yamlFile = urlHelper.getFileForMapTile(name);
        if (!yamlFile.isFile()) {
            throw new InvalidInputException("Did not find resource for map object " + name);
        }

        File imageFile = urlHelper.getImageFileForMapTile(name);
        if (!imageFile.isFile()) {
            throw new InvalidInputException("Did not find image resource for map object " + name);
        }

        return load(yamlFile, imageFile);
    }

    RawMapTileDto load(File yamlFile, File imageFile) throws InvalidInputException {
        try {
            RawMapTileDto mapTileDto = objectMapper.readValue(yamlFile, RawMapTileDto.class);
            mapTileDto.setImageData(new ImageData(new FileInputStream(imageFile)));
            mapTileDto.setImage(imageLoader.load(imageFile));
            return mapTileDto;
        } catch (IOException e) {
            throw new InvalidInputException("Could not read map tile " + yamlFile, e);
        }
    }

    public RawMapTileDto load(MapTileSource mapTileSource) throws InvalidInputException {
        return load(mapTileSource.getYamlFile(), mapTileSource.getImageFile());
    }

    public Try<List<File>> save(RawMapTileDto mapTileDto) {
        String tileName = mapTileDto.getName();
        File yamlFile = urlHelper.getFileForMapTile(tileName);
        File imageFile = urlHelper.getImageFileForMapTile(tileName);

        try {
            String yamlFileContent = Files.readString(yamlFile.toPath());
            if (!yamlFileContent.isEmpty()) {
                return Try.failure(new InvalidInputException("yaml file for tile " + tileName + " already contains content"));
            }
        } catch (IOException ignored) {
        }

        try {
            String imageFileContent = Files.readString(imageFile.toPath());
            if (!imageFileContent.isEmpty()) {
                return Try.failure(new InvalidInputException("image file for tile " + tileName + " already contains content"));
            }
        } catch (IOException ignored) {
        }

        return Try(writeTileDataTo(mapTileDto, yamlFile))
            .mapTry(writeImageTo(mapTileDto.getImageData(), imageFile));
    }

    private CheckedFunction0<List<File>> writeTileDataTo(RawMapTileDto mapTileDto, File yamlFile) {
        return () -> {
            objectMapper.writeValue(yamlFile, mapTileDto);
            return List.of(yamlFile);
        };
    }

    private CheckedFunction1<List<File>, List<File>> writeImageTo(ImageData imageData, File imageFile) {
        return savedFiles -> {
            var imageLoader = new org.eclipse.swt.graphics.ImageLoader();
            imageLoader.data = new ImageData[]{imageData};
            imageLoader.save(imageFile.getAbsolutePath(), SWT.IMAGE_PNG);
            return savedFiles.append(imageFile);
        };
    }
}
