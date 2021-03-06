package manfred.manfreditor.mapobject;

import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.Map;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.mockito.Mockito.mock;

class NoneTest {

    @Test
    void testInsertAccessibility() {
        None underTest = new None();
        HashMap<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();

        underTest.insertAccessibilityIndicatorsAt(mock(Map.TileCoordinate.class), mergedAccessibility);

        assertThat(mergedAccessibility, anEmptyMap());
    }
}