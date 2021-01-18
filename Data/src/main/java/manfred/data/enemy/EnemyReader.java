package manfred.data.enemy;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.ObjectReader;
import manfred.data.image.ImageLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class EnemyReader implements ObjectReader<EnemyDto> {

    private final ImageLoader imageLoader;
    private final ObjectMapper objectMapper;

    public EnemyReader(ImageLoader imageLoader, ObjectMapper objectMapper) {
        this.imageLoader = imageLoader;
        this.objectMapper = objectMapper;
    }

    public EnemyDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/enemies/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Did not find resource for enemy " + name);
        }

        URL imageURL = getClass().getResource("/enemies/" + name + ".png");
        if (imageURL == null) {
            throw new InvalidInputException("Did not find image resource for enemy " + name);
        }

        return load(yamlURL, imageURL);
    }

    EnemyDto load(URL yamlURL, URL imageURL) throws InvalidInputException {
        try {
            EnemyDto enemyDto = objectMapper.readValue(yamlURL, EnemyDto.class);
            enemyDto.setImage(imageLoader.load(imageURL));
            return enemyDto;
        } catch (IOException e) {
            throw new InvalidInputException("Could not read enemy " + yamlURL, e);
        }
    }
}
