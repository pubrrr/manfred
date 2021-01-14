package manfred.data.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class RawMapReader {

    private final ObjectMapper objectMapper;

    public RawMapReader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RawMapDto load(String name) throws InvalidInputException {
        URL yamlURL = getClass().getResource("/maps/" + name + ".yaml");
        if (yamlURL == null) {
            throw new InvalidInputException("Did not find resource for map " + name);
        }

        return load(yamlURL);
    }

    RawMapDto load(URL yamlURL) throws InvalidInputException {
        try {
            return objectMapper.readValue(yamlURL, RawMapDto.class);
        } catch (IOException e) {
            throw new InvalidInputException("Could not read map from " + yamlURL, e);
        }
    }
}
