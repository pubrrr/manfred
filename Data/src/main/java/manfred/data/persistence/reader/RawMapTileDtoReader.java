package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.RawMapTileDto;
import org.eclipse.swt.graphics.ImageData;
import org.springframework.stereotype.Component;

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

        return load(yamlURL, imageURL);
    }

    RawMapTileDto load(URL yamlURL, URL imageURL) throws InvalidInputException {
        try {
            RawMapTileDto mapTileDto = objectMapper.readValue(yamlURL, RawMapTileDto.class);
            mapTileDto.setImageData(new ImageData(imageURL.openStream()));
            mapTileDto.setImage(imageLoader.load(imageURL));
            return mapTileDto;
        } catch (IOException e) {
            throw new InvalidInputException("Could not read map tile " + yamlURL, e);
        }
    }

    public RawMapTileDto load(MapTileSource mapTileSource) throws InvalidInputException {
        return load(mapTileSource.getTileUrl(), mapTileSource.getImageUrl());
    }
}
