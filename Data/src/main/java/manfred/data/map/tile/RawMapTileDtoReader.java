package manfred.data.map.tile;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.ObjectReader;
import manfred.data.image.ImageLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class RawMapTileDtoReader implements ObjectReader<RawMapTileDto> {

    private final ObjectMapper objectMapper;
    private final ImageLoader imageLoader;

    public RawMapTileDtoReader(ObjectMapper objectMapper, ImageLoader imageLoader) {
        this.objectMapper = objectMapper;
        this.imageLoader = imageLoader;
    }

    @Override
    public RawMapTileDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/maps/tiles/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Did not find resource for enemy " + name);
        }

        URL imageURL = getClass().getResource("/maps/tiles/" + name + ".png");
        if (imageURL == null) {
            throw new InvalidInputException("Did not find image resource for enemy " + name);
        }

        return load(yamlURL, imageURL);
    }

    RawMapTileDto load(URL yamlURL, URL imageURL) throws InvalidInputException {
        try {
            RawMapTileDto mapTileDto = objectMapper.readValue(yamlURL, RawMapTileDto.class);
            mapTileDto.setImage(imageLoader.load(imageURL));
            return mapTileDto;
        } catch (IOException e) {
            throw new InvalidInputException("Could not read map tile " + yamlURL, e);
        }
    }
}
