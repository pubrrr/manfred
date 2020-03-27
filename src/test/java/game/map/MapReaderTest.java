package game.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapReaderTest {
    static MapReader underTest;

    @BeforeAll
    static void init() {
        underTest = new MapReader();
    }

    @Test
    public void convertsString() {
        Map result = underTest.convert("{\"name\":\"test\",\"map\":[[]]}");
        assertEquals("test", result.getName());
        assertArrayEquals(new String[0][0], result.getMap());
    }
}
