package manfred.manfreditor.map.accessibility;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.mockMapPrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

class AccessibilityMergerTest {

    private AccessibilityMerger underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccessibilityMerger();
    }

    @Test
    void mergeEmptyInput() {
        var result = underTest.merge(HashMap.empty());

        assertThat(result.size(), is(0));
    }

    @Test
    void merge() {
        MapPrototype structureMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.notAccessible(),
            coordinatePrototype(0, 1), TilePrototype.accessible(),
            coordinatePrototype(1, 1), TilePrototype.notAccessible()
        ));
        ConcreteMapObject mapObject = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);

        Map<TileCoordinate, MapObject> input = HashMap.of(
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

        Map<TileCoordinate, AccessibilityIndicator> result = underTest.merge(input);

        assertThat(result.size(), is(9));
        assertThat(result.get(tileCoordinate(0, 0)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(1, 0)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(2, 0)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(0, 1)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(1, 1)).get(), instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(2, 1)).get(), instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(0, 2)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(1, 2)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(result.get(tileCoordinate(2, 2)).get(), instanceOf(ColoredAccessibilityIndicator.class));

        assertThat(result.get(tileCoordinate(0, 0)).get().getSource().isEmpty(), is(true));
        assertThat(result.get(tileCoordinate(1, 1)).get().getSource(), equalTo(Optional.of(new Source("name", tileCoordinate(1, 1)))));
        assertThat(result.get(tileCoordinate(2, 1)).get().getSource(), equalTo(Optional.of(new Source("name", tileCoordinate(1, 1)))));
        assertThat(result.get(tileCoordinate(2, 2)).get().getSource(), equalTo(Optional.of(new Source("name", tileCoordinate(1, 1)))));
    }

    @Test
    void mergeObjectThatReachesOverTopAndRightBorderOfMap() {
        MapPrototype structureMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.notAccessible(),
            coordinatePrototype(0, 1), TilePrototype.accessible(),
            coordinatePrototype(1, 1), TilePrototype.notAccessible()
        ));
        ConcreteMapObject mapObject = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);

        Map<TileCoordinate, MapObject> input = HashMap.of(tileCoordinate(0, 0), mapObject);

        Map<TileCoordinate, AccessibilityIndicator> result = underTest.merge(input);

        assertThat(result.size(), is(1));
        AccessibilityIndicator objectAt_0_0 = result.get(tileCoordinate(0, 0)).get();
        assertThat(objectAt_0_0, instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(objectAt_0_0.getSource(), is(Optional.of(new Source("name", tileCoordinate(0, 0)))));
    }
}