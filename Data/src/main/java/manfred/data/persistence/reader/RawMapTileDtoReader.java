package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.RawMapTileDto;
import org.eclipse.swt.graphics.ImageData;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Component
public class RawMapTileDtoReader {

    private final ObjectMapper objectMapper;
    private final ImageLoader imageLoader;

    public RawMapTileDtoReader(ObjectMapper objectMapper, ImageLoader imageLoader) {
        this.objectMapper = objectMapper;
        this.imageLoader = imageLoader;
    }

    public RawMapTileDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/maps/tiles/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Did not find resource for map object " + name);
        }

        URL imageURL = getClass().getResource("/maps/tiles/" + name + ".png");
        if (imageURL == null) {
            throw new InvalidInputException("Did not find image resource for map object " + name);
        }

        return load(new File(yamlURL.getFile()), new File(imageURL.getFile()));
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
}
