package manfred.game.map;

import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MapReaderTest {
    static MapReader underTest;

    @BeforeAll
    static void init() {
        underTest = new MapReader();
    }

    @Test
    public void convert() throws InvalidInputException {
        Map result = underTest.convert("{\"name\":\"test\",\"map\":[[]]}");

        assertEquals("test", result.getName());
        assertArrayEquals(new String[1][0], result.getArray());
    }

    @Test
    public void convertsMapWithString() throws InvalidInputException {
        String jsonWithStrings = "{\"name\":\"test\",\"map\":[[\"opa\", \"0\"]]}";
        String[][] expectedMap = {{"opa", "0"}};

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    public void convertsMapWithInt() throws InvalidInputException {
        String jsonWithIntMap = "{\"name\":\"test\",\"map\":[[0, 1], [1, 0]]}";
        String[][] expectedMap = {{"0", "1"}, {"1", "0"}};

        Map result = underTest.convert(jsonWithIntMap);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    public void convertsMapWithStringAndInt() throws InvalidInputException {
        String jsonWithStrings = "{\"name\":\"test\",\"map\":[[\"opa\", 0], [1,1], [\"a\", 5]]}";
        String[][] expectedMap = {{"opa", "0"}, {"1", "1"}, {"a", "5"}};

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    public void readAndConvert() throws InvalidInputException, IOException {
        String[][] expectedMap = {{"opa", "0"}, {"1", "1"}, {"a", "5"}};

        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
        Map result = underTest.convert(json);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    public void expectsRectangularMap() {
        final String invalidJson = "{\"name\":\"test\",\"map\":[[0, 1], [0]]}";

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }
}
