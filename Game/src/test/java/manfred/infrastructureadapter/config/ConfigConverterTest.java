package manfred.infrastructureadapter.config;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.ConfigDto;
import manfred.data.persistence.dto.GelaberBoxPositionDto;
import manfred.data.persistence.dto.WindowSizeDto;
import manfred.data.shared.PositiveInt;
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
            new WindowSizeDto(PositiveInt.ofNonZero(1), PositiveInt.ofNonZero(2)),
            new GelaberBoxPositionDto(PositiveInt.of(7), PositiveInt.of(8)),
            PositiveInt.ofNonZero(3),
            PositiveInt.of(4),
            PositiveInt.of(5),
            PositiveInt.of(6)
        ));

        assertEquals(1, result.getWindowWidth());
        assertEquals(2, result.getWindowHeight());
        assertEquals(PositiveInt.ofNonZero(3), result.getPixelBlockSize());
        assertEquals(4, result.getTextBoxDistanceToBorder());
        assertEquals(5, result.getTextPointSize());
        assertEquals(6, result.getTextDistanceToBox());
        assertEquals(7, result.getGelaberBoxPositionX());
        assertEquals(8, result.getGelaberBoxPositionY());
    }
}