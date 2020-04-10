package manfred.game.map;

import manfred.game.exception.InvalidInputException;
import manfred.game.interact.PersonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MapReaderTest {
    private MapReader underTest;
    private PersonReader personReaderMock;


    @BeforeEach
    void init() {
        personReaderMock = mock(PersonReader.class);

        underTest = new MapReader(personReaderMock);
    }

    @Test
    void convert() throws InvalidInputException {
        Map result = underTest.convert("{\"name\":\"test\",\"map\":[[]]}");

        assertEquals("test", result.getName());
        assertArrayEquals(new String[1][0], result.getArray());
    }

    @Test
    void convertsMapWithString() throws InvalidInputException {
        String jsonWithStrings = "{\"name\":\"test\",\"map\":[[\"1\", \"0\"]]}";
        String[][] expectedMap = {{"1", "0"}};

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    void convertsMapWithInt() throws InvalidInputException {
        String jsonWithIntMap = "{\"name\":\"test\",\"map\":[[0, 1], [1, 0]]}";
        String[][] expectedMap = {{"0", "1"}, {"1", "0"}};

        Map result = underTest.convert(jsonWithIntMap);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    void convertsMapWithStringAndInt() throws InvalidInputException {
        String jsonWithStrings = "{\"name\":\"test\",\"map\":[[\"0\", 0], [1,1], [\"1\", 1]]}";
        String[][] expectedMap = {{"0", "0"}, {"1", "1"}, {"1", "1"}};

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    void readAndConvert() throws InvalidInputException, IOException {
        String[][] expectedMap = {{"opa", "0"}, {"1", "1"}, {"0", "1"}};

        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
        Map result = underTest.convert(json);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    void expectsRectangularMap() {
        final String invalidJson = "{\"name\":\"test\",\"map\":[[0, 1], [0]]}";

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }

    @Test
    void triggersLoadPerson() throws IOException, InvalidInputException {
        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
        underTest.convert(json);

        verify(personReaderMock).load("opa");
    }
}
