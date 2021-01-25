package manfred.infrastructureadapter.config;

import manfred.data.persistence.dto.ConfigDto;
import manfred.data.persistence.dto.GelaberBoxPositionDto;
import manfred.data.persistence.dto.WindowSizeDto;
import manfred.game.config.GameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigConverterTest {

    private ConfigConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new ConfigConverter();
    }

    @Test
    void convert() {
        GameConfig result = underTest.convert(new ConfigDto(
            new WindowSizeDto(1, 2),
            new GelaberBoxPositionDto(7, 8),
            3,
            4,
            5,
            6
        ));

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