package manfred.manfreditor.map;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.Source;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.mockMapPrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static manfred.manfreditor.map.ObjectInsertionValidator.Result;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjectInsertionValidatorTest {

    private ObjectInsertionValidator underTest;

    @BeforeEach
    void setUp() {
        underTest = new ObjectInsertionValidator();
    }

    @Test
    void objectAndMapAreAlwaysAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.accessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator());

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertTrue(result.wasSuccessful());
        assertThat(result.getValidationMessages(), empty());
    }

    @Test
    void objectTileIsNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator());

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertTrue(result.wasSuccessful());
        assertThat(result.getValidationMessages(), empty());
    }

    @Test
    void mapIsNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.accessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(tileCoordinate(0, 0), new ColoredAccessibilityIndicator(null, null));

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertTrue(result.wasSuccessful());
        assertThat(result.getValidationMessages(), empty());
    }

    @Test
    void mapAndObjectAreNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(
            tileCoordinate(0, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        );

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertFalse(result.wasSuccessful());
        assertThat(result.getValidationMessages(), containsInAnyOrder("Tile (0,0) is not accessible, blocked by object tileName at (0,0)"));
    }

    @Test
    void twoTileObjectAndBothTilesAreNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.notAccessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(
            tileCoordinate(0, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0))),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        );

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertFalse(result.wasSuccessful());
        assertThat(result.getValidationMessages(), containsInAnyOrder(
            "Tile (0,0) is not accessible, blocked by object tileName at (0,0)",
            "Tile (1,0) is not accessible, blocked by object tileName at (0,0)")
        );
    }

    @Test
    void twoTileObjectAndOneTileAreNotAccessibleOnMap() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.notAccessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        );

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertFalse(result.wasSuccessful());
        assertThat(result.getValidationMessages(), containsInAnyOrder("Tile (1,0) is not accessible, blocked by object tileName at (0,0)"));
    }

    @Test
    void twoTileObject_oneTileAccessibleOnMap_otherTileAccessibleOnObjectStructure() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.accessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        );

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), mergedAccessibility);

        assertTrue(result.wasSuccessful());
        assertThat(result.getValidationMessages(), empty());
    }

    @Test
    void objectAtAnotherTileThanOrigin() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, null);
        Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility = Map.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(3, 0)))
        );

        Result result = underTest.mayObjectBeInserted(object, tileCoordinate(1, 0), mergedAccessibility);

        assertFalse(result.wasSuccessful());
        assertThat(result.getValidationMessages(), containsInAnyOrder("Tile (1,0) is not accessible, blocked by object tileName at (3,0)"));
    }
}