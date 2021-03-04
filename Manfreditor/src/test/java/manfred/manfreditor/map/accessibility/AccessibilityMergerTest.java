package manfred.manfreditor.map.accessibility;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.mockMapPrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.instanceOf;

class AccessibilityMergerTest {

    private AccessibilityMerger underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccessibilityMerger();
    }

    @Test
    void merge() {
        MapPrototype structureMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.notAccessible(),
            coordinatePrototype(0, 1), TilePrototype.accessible(),
            coordinatePrototype(1, 1), TilePrototype.notAccessible()
        ));
        ConcreteMapObject mapObject = new ConcreteMapObject(
            "name",
            structureMock,
            null
        );

        java.util.Map<Map.TileCoordinate, MapObject> input = java.util.Map.of(
            tileCoordinate(0, 0), MapObject.none(),
            tileCoordinate(1, 0), MapObject.none(),
            tileCoordinate(2, 0), MapObject.none(),
            tileCoordinate(0, 1), MapObject.none(),
            tileCoordinate(1, 1), mapObject,
            tileCoordinate(2, 1), MapObject.none(),
            tileCoordinate(0, 2), MapObject.none(),
            tileCoordinate(1, 2), MapObject.none(),
            tileCoordinate(2, 2), MapObject.none()
        );

        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> result = underTest.merge(input);

        assertThat(result, aMapWithSize(9));
        assertThat(result.get(tileCoordinate(0, 0)), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(1, 0)), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(2, 0)), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(0, 1)), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(1, 1)), instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(2, 1)), instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(0, 2)), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(1, 2)), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(2, 2)), instanceOf(ColoredAccessibilityIndicator.class));
    }
}