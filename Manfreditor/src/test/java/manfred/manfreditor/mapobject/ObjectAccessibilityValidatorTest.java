package manfred.manfreditor.mapobject;

import io.vavr.collection.HashMap;
import io.vavr.control.Validation;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.mapobject.NewMapObjectModel.PreviewTileCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ObjectAccessibilityValidatorTest {

    private ObjectAccessibilityValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new ObjectAccessibilityValidator();
    }

    @Test
    void oneInaccessibleTile_isValid() {
        AccessibilityGrid input = new AccessibilityGrid(HashMap.of(previewTileCoordinate(0, 0), false));

        Validation<String, AccessibilityGrid> result = underTest.validate(input);

        assertThat(result.isValid(), is(true));
        assertThat(result.get(), is(input));
    }

    @Test
    void fourInaccessibleTiles_isValid() {
        AccessibilityGrid input = new AccessibilityGrid(HashMap.of(
            previewTileCoordinate(0, 0), false,
            previewTileCoordinate(1, 0), false,
            previewTileCoordinate(0, 1), false,
            previewTileCoordinate(1, 1), false
        ));

        Validation<String, AccessibilityGrid> result = underTest.validate(input);

        assertThat(result.isValid(), is(true));
        assertThat(result.get(), is(input));
    }

    @Test
    void leftmostColumnIsAccessible_isInvalid() {
        AccessibilityGrid input = new AccessibilityGrid(HashMap.of(
            previewTileCoordinate(0, 0), true,
            previewTileCoordinate(1, 0), false,
            previewTileCoordinate(0, 1), true,
            previewTileCoordinate(1, 1), false
        ));

        Validation<String, AccessibilityGrid> result = underTest.validate(input);

        assertThat(result.isValid(), is(false));
        assertThat(result.getError(), is("at least one tile in the left column must be inaccessible"));
    }

    private PreviewTileCoordinate previewTileCoordinate(int x, int y) {
        return new PreviewTileCoordinate(PositiveInt.of(x), PositiveInt.of(y));
    }
}