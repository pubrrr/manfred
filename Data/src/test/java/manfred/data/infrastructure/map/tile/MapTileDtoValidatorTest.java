package manfred.data.infrastructure.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.RawMapTileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapTileDtoValidatorTest {

    private MapTileDtoValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new MapTileDtoValidator(new AccessibilityTileConverter());
    }

    @Test
    void emptyInput() {
        RawMapTileDto input = new RawMapTileDto("tileName", List.of(), null, null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Validation of map tile tileName failed"));
        assertThat(exception.getCause().getMessage(), containsString("must not be empty"));
    }

    @Test
    void invalidInput() {
        RawMapTileDto input = new RawMapTileDto("tileName", List.of("0", "1,1"), null, null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.validate(input));
        assertThat(exception.getMessage(), containsString("Validation of map tile tileName failed"));
    }

    @Test
    void validInput() throws InvalidInputException {
        RawMapTileDto input = new RawMapTileDto("tileName", List.of("0,1", "0,0"), null, null);

        ValidatedMapTileDto result = underTest.validate(input);

        assertTrue(result.getStructure().get(1, 1).isAccessible());
        assertFalse(result.getStructure().get(0, 1).isAccessible());
    }
}