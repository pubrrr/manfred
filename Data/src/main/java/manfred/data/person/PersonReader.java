package manfred.data.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.image.ImageLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class PersonReader {
    private final ImageLoader imageLoader;
    private final ObjectMapper objectMapper;

    public PersonReader(ImageLoader imageLoader, ObjectMapper objectMapper) {
        this.imageLoader = imageLoader;
        this.objectMapper = objectMapper;
    }

    public PersonDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/persons/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Resource for " + "'/persons/" + name + ".yaml' no found");
        }
        URL imageURL = getClass().getResource("/persons/" + name + ".png");
        if (imageURL == null) {
            throw new InvalidInputException("Resource for " + "'/persons/" + name + ".png' no found");
        }
        return load(yamlURL, imageURL);
    }

    PersonDto load(URL yamlURL, URL imageURL) throws InvalidInputException {
        try {
            PersonDto personDto = objectMapper.readValue(yamlURL, PersonDto.class);
            personDto.setImage(imageLoader.load(imageURL));
            return personDto;
        } catch (IOException e) {
            throw new InvalidInputException(e);
        }
    }
}
