package manfred.game;

import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigReaderTest {
    private ConfigReader underTest;

    @BeforeEach
    void init() {
        underTest = new ConfigReader();
    }

    @Test
    void convert() throws InvalidInputException {
        String input = "{window: {width: 1, height: 2}, pixelBlockSize: 3}";

        GameConfig result = underTest.convert(input);

        assertEquals(1, result.getWindowWidth());
        assertEquals(2, result.getWindowHeight());
        assertEquals(3, result.getPixelBlockSize());
    }
}