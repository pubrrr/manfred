package manfred.game;

import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigReaderTest {
    private ConfigReader underTest;

    @BeforeEach
    void init() {
        underTest = new ConfigReader();
    }

    @Test
    void convert() throws InvalidInputException {
        String input = "{window: {width: 1, height: 2}, pixelBlockSize: 3, textBoxDistanceToBorder: 4, textPointSize: 5, textDistanceToBox: 6, gelaber: {boxPositionX: 7, boxPositionY: 8}}";

        GameConfig result = underTest.convert(input);

        assertEquals(1, result.getWindowWidth());
        assertEquals(2, result.getWindowHeight());
        assertEquals(3, result.getPixelBlockSize());
        assertEquals(4, result.getTextBoxDistanceToBorder());
        assertEquals(5, result.getTextPointSize());
        assertEquals(6, result.getTextDistanceToBox());
        assertEquals(7, result.getGelaberBoxPositionX());
        assertEquals(8, result.getGelaberBoxPositionY());
    }
}