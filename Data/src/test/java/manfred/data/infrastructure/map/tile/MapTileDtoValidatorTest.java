package manfred.data.infrastructure.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.persistence.dto.RawMapTileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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

        MapTileStructurePrototype structure = result.getStructure();
        List<MapPrototype.Coordinate> coordinateSet = structure.getCoordinateSet();
        assertThat(coordinateSet, hasSize(4));

        MapPrototype.Coordinate coordinate_1_1 = coordinateSet.stream()
            .filter(coordinate -> coordinate.getX().value() == 1 && coordinate.getY().value() == 1)
            .findAny()
            .orElseThrow(() -> new AssertionFailedError("Coordinate not found"));

        MapPrototype.Coordinate coordinate_0_1 = coordinateSet.stream()
            .filter(coordinate -> coordinate.getX().value() == 0 && coordinate.getY().value() == 1)
            .findAny()
            .orElseThrow(() -> new AssertionFailedError("Coordinate not found"));

        assertTrue(structure.getFromMap(coordinate_1_1).isAccessible());
        assertFalse(structure.getFromMap(coordinate_0_1).isAccessible());
    }
}