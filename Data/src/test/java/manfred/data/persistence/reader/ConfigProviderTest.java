package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.ConfigDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

class ConfigProviderTest {
    private ConfigProvider underTest;

    @BeforeEach
    void init() {
        underTest = new ConfigProvider(new ObjectMapper(new YAMLFactory()));
    }

    @Test
    void provide() throws InvalidInputException {
        ConfigDto result = underTest.provide();

        assertThat(result, notNullValue());
    }

    @Test
    void readNonExistentFile() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.readFromUrl((new File("unknown")).toURI().toURL()));
        assertThat(exception.getMessage(), containsString("Could not read configuration file"));
    }
}