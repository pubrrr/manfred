package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.CheckedFunction0;
import io.vavr.CheckedRunnable;
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

    public Try<Void> save(RawMapTileDto mapTileDto) {
        File yamlFile = urlHelper.getFileForMapTile(mapTileDto.getName());
        File imageFile = urlHelper.getImageFileForMapTile(mapTileDto.getName());

        return Try(writeTileDataTo(mapTileDto, yamlFile))
            .andThenTry(writeImageTo(mapTileDto.getImageData(), imageFile));
    }

    private CheckedFunction0<Void> writeTileDataTo(RawMapTileDto mapTileDto, File yamlFile) {
        return () -> {
            objectMapper.writeValue(yamlFile, mapTileDto);
            return null;
        };
    }

    private CheckedRunnable writeImageTo(ImageData imageData, File imageFile) {
        return () -> {
            var imageLoader = new org.eclipse.swt.graphics.ImageLoader();
            imageLoader.data = new ImageData[]{imageData};
            imageLoader.save(imageFile.getAbsolutePath(), SWT.IMAGE_PNG);
        };
    }
}
