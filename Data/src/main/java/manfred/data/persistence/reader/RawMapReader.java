package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.RawMapDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class RawMapReader {

    private final ObjectMapper objectMapper;
    private final UrlHelper urlHelper;

    public RawMapDto load(String name) throws InvalidInputException {
        URL yamlURL = urlHelper.getResourceForMap(name).orElseThrow(invalidInputException(name));

        return load(yamlURL);
    }

    RawMapDto load(URL yamlURL) throws InvalidInputException {
        try {
            return objectMapper.readValue(yamlURL, RawMapDto.class);
        } catch (IOException e) {
            throw new InvalidInputException("Could not read map from " + yamlURL, e);
        }
    }

    private Supplier<InvalidInputException> invalidInputException(String name) {
        return () -> new InvalidInputException("Did not find resource for map " + name);
    }
}
