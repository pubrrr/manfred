package manfred.manfreditor.map;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class MapModelTest {

    @Test
    void setupWithEmptyMap() {
        MapModel underTest = new MapModel(new Map("name", new HashMap<>()), new AccessibilityMerger());
    }
}