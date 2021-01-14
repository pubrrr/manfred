package manfred.data.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RawMapReaderTest {

    private RawMapReader underTest;

    @BeforeEach
    void init() {
        underTest = new RawMapReader(new ObjectMapper(new YAMLFactory()));
    }

    @Test
    void testConvert() throws InvalidInputException {
        URL url = getClass().getResource("/maps/testMap.yaml");
        RawMapDto result = underTest.load(url);

        assertThat(result.getName(), equalTo("Wald"));
        assertThat(result.getMap(), hasSize(2));
        assertThat(result.getPortals(), hasSize(1));
        assertThat(result.getDoors(), hasSize(2));
        assertThat(result.getEnemies(), hasSize(1));
    }

    @Test
    void unknownInputFile() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.load("unknown"));
        assertThat(exception.getMessage(), containsString("Did not find resource for map"));
    }
}