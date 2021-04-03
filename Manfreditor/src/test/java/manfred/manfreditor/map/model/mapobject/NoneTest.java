package manfred.manfreditor.map.model.mapobject;

import io.vavr.collection.Map;
import io.vavr.control.Either;
import manfred.manfreditor.map.model.Map.TileCoordinate;
import manfred.manfreditor.map.model.accessibility.AccessibilityIndicator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class NoneTest {

    @Test
    void testInsertAccessibility() {
        None underTest = new None();
        HashMap<TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();

        Either<String, Map<TileCoordinate, AccessibilityIndicator>> structure = underTest.getStructureAt(mock(TileCoordinate.class));

        assertThat(structure.isRight(), is(true));
        assertThat(structure.get().size(), is(0));
    }
}