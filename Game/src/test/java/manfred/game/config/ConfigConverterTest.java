package manfred.game.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigConverterTest {

    private ConfigConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new ConfigConverter();
    }

    @Test
    void convert() {
        GameConfig result = underTest.convert(null);

        Assertions.assertEquals(1, result.getWindowWidth());
        Assertions.assertEquals(2, result.getWindowHeight());
        Assertions.assertEquals(3, result.getPixelBlockSize());
        Assertions.assertEquals(4, result.getTextBoxDistanceToBorder());
        Assertions.assertEquals(5, result.getTextPointSize());
        Assertions.assertEquals(6, result.getTextDistanceToBox());
        Assertions.assertEquals(7, result.getGelaberBoxPositionX());
        Assertions.assertEquals(8, result.getGelaberBoxPositionY());
    }
}