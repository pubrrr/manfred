package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.ConfigDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class ConfigProvider {
    private final ObjectMapper objectMapper;

    public ConfigProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ConfigDto provide() throws InvalidInputException {
        return readFromUrl(getConfigFile());
    }

    ConfigDto readFromUrl(URL src) throws InvalidInputException {
        try {
            return objectMapper.readValue(src, ConfigDto.class);
        } catch (IOException e) {
            throw new InvalidInputException("Could not read configuration file " + src, e);
        }
    }

    private URL getConfigFile() {
        return getClass().getResource("/config/config.yaml");
    }
}
