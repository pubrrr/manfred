package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.persistence.ObjectReader;
import manfred.data.persistence.dto.EnemyDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

@Component
public class EnemyReader implements ObjectReader<EnemyDto> {

    private final ImageLoader imageLoader;
    private final ObjectMapper objectMapper;
    private final UrlHelper urlHelper;

    public EnemyReader(ImageLoader imageLoader, ObjectMapper objectMapper, UrlHelper urlHelper) {
        this.imageLoader = imageLoader;
        this.objectMapper = objectMapper;
        this.urlHelper = urlHelper;
    }

    @Override
    public EnemyDto load(String name) throws InvalidInputException {
        return load(
            urlHelper.getResourceForEnemy(name).orElseThrow(invalidInputException("Resource for enemy " + name + " not found")),
            urlHelper.getImageResourceForEnemy(name).orElseThrow(invalidInputException("Image resource for enemy " + name + " not found"))
        );
    }

    private Supplier<InvalidInputException> invalidInputException(String message) {
        return () -> new InvalidInputException(message);
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
