package manfred.manfreditor.mapobject;

import io.vavr.control.Either;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.Map;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.mockMapPrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

public class ConcreteMapObjectTest {

    @Test
    void insertAccessibility_objectStructureHasOnlyOneTile() {
        MapPrototype mapPrototypeMock = mockMapPrototype(new HashMap<>());
        ConcreteMapObject underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 0), null);
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();

        underTest.insertAccessibilityIndicatorsAt(tileCoordinate(0, 0), mergedAccessibility);

        assertThat(mergedAccessibility, aMapWithSize(0));
    }

    @Test
    void insertAccessibility_objectStructureHasOnlyOneAccessibleTile() {
        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.accessible()
        ));

        ConcreteMapObject underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 0), null);
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();

        underTest.insertAccessibilityIndicatorsAt(tileCoordinate(0, 0), mergedAccessibility);

        assertThat(mergedAccessibility, aMapWithSize(0));
    }

    @Test
    void insertAccessibility_objectStructureHasOnlyOneNonAccessibleTile() {
        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible()
        ));

        ConcreteMapObject underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 0), null);
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();

        Map.TileCoordinate tileCoordinate = tileCoordinate(0, 0);
        underTest.insertAccessibilityIndicatorsAt(tileCoordinate, mergedAccessibility);

        assertThat(mergedAccessibility, aMapWithSize(1));
        assertThat(mergedAccessibility.get(tileCoordinate), instanceOf(ColoredAccessibilityIndicator.class));
    }

    @Test
    void insertAccessibility_objectStructureHasOneNonAccessibleAndOneAccessibleTile() {
        MapPrototype.Coordinate coordinatePrototype_1_0 = coordinatePrototype(1, 0);
        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.accessible(),
            coordinatePrototype_1_0, TilePrototype.notAccessible()
        ));

        ConcreteMapObject underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 0), null);
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();

        Map.TileCoordinate tileCoordinate = tileCoordinate(0, 0);
        underTest.insertAccessibilityIndicatorsAt(tileCoordinate, mergedAccessibility);

        assertThat(mergedAccessibility, aMapWithSize(1));
        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype_1_0)), instanceOf(ColoredAccessibilityIndicator.class));
    }

    @Test
    void insertAccessibility_givenInitializedInput_insertsNonAccessibleTiles() {
        MapPrototype.Coordinate coordinatePrototype_1_0 = coordinatePrototype(1, 0);
        MapPrototype.Coordinate coordinatePrototype_1_1 = coordinatePrototype(1, 1);

        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), TilePrototype.accessible(),
            coordinatePrototype_1_0, TilePrototype.notAccessible(),
            coordinatePrototype(0, 1), TilePrototype.accessible(),
            coordinatePrototype_1_1, TilePrototype.notAccessible()
        ));

        Map.TileCoordinate tileCoordinate = tileCoordinate(0, 0);

        ConcreteMapObject underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 0), null);
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(0, 0)), new EmptyAccessibilityIndicator());
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(1, 0)), new EmptyAccessibilityIndicator());
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(2, 0)), new EmptyAccessibilityIndicator());
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(0, 1)), new EmptyAccessibilityIndicator());
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(1, 1)), new EmptyAccessibilityIndicator());
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(2, 1)), new EmptyAccessibilityIndicator());

        underTest.insertAccessibilityIndicatorsAt(tileCoordinate, mergedAccessibility);

        assertThat(mergedAccessibility, aMapWithSize(6));
        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype_1_0)), instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype_1_1)), instanceOf(ColoredAccessibilityIndicator.class));

        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype(0, 0))), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype(0, 1))), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype(2, 0))), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(mergedAccessibility.get(tileCoordinate.translateBy(coordinatePrototype(2, 1))), instanceOf(EmptyAccessibilityIndicator.class));
    }

    @Test
    void insertAccessibility_withOffset() {
        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));

        Map.TileCoordinate tileCoordinate = tileCoordinate(0, 1);

        ConcreteMapObject underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 1), null);
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = new HashMap<>();
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(0, 0)), new EmptyAccessibilityIndicator());
        mergedAccessibility.put(tileCoordinate.translateBy(coordinatePrototype(0, 1)), new EmptyAccessibilityIndicator());

        underTest.insertAccessibilityIndicatorsAt(tileCoordinate, mergedAccessibility);

        assertThat(mergedAccessibility.get(tileCoordinate(0, 0)), instanceOf(ColoredAccessibilityIndicator.class));
    }

    @Test
    void getStructureAt_withOffset() {
        TilePrototype tileMock_0_0 = mock(TilePrototype.class);
        TilePrototype tileMock_1_0 = mock(TilePrototype.class);
        TilePrototype tileMock_0_1 = mock(TilePrototype.class);
        TilePrototype tileMock_1_1 = mock(TilePrototype.class);


        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), tileMock_0_0,
            coordinatePrototype(1, 0), tileMock_1_0,
            coordinatePrototype(0, 1), tileMock_0_1,
            coordinatePrototype(1, 1), tileMock_1_1
        ));

        var underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 1), null);
        Either<String, java.util.Map<Map.TileCoordinate, TilePrototype>> structure = underTest.getStructureAt(tileCoordinate(1, 1));

        assertThat(structure.get().get(tileCoordinate(1, 0)), is(tileMock_0_0));
        assertThat(structure.get().get(tileCoordinate(1, 1)), is(tileMock_0_1));
        assertThat(structure.get().get(tileCoordinate(2, 0)), is(tileMock_1_0));
        assertThat(structure.get().get(tileCoordinate(2, 1)), is(tileMock_1_1));
    }

    @Test
    void getStructureAt_fails() {
        var underTest = new ConcreteMapObject("name", null, coordinatePrototype(0, 1), null);
        Either<String, java.util.Map<Map.TileCoordinate, TilePrototype>> structure = underTest.getStructureAt(tileCoordinate(0, 0));

        assertThat(structure.isLeft(), is(true));
        assertThat(structure.getLeft(), is("Object location must not result in negative coordinates, given: (0,0), origin is (0,1)"));
    }
}
