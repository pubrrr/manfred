package manfred.manfreditor.map.model;

import io.vavr.collection.HashMap;
import io.vavr.control.Validation;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.map.model.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.Source;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.mockMapPrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
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
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator()), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertTrue(result.isValid());
    }

    @Test
    void objectTileIsNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator()), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertTrue(result.isValid());
    }

    @Test
    void mapIsNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.accessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(tileCoordinate(0, 0), new ColoredAccessibilityIndicator(null, null)), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertTrue(result.isValid());
    }

    @Test
    void mapAndObjectAreNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        ), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertFalse(result.isValid());
        assertThat(result.getError(), containsInAnyOrder("Tile (0,0) is not accessible, blocked by object tileName at (0,0)"));
    }

    @Test
    void twoTileObjectAndBothTilesAreNotAccessible() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.notAccessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0))),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        ), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertFalse(result.isValid());
        assertThat(result.getError(), containsInAnyOrder(
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
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        ), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertFalse(result.isValid());
        assertThat(result.getError(), containsInAnyOrder("Tile (1,0) is not accessible, blocked by object tileName at (0,0)"));
    }

    @Test
    void twoTileObject_oneTileAccessibleOnMap_otherTileAccessibleOnObjectStructure() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(1, 0), TilePrototype.accessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(0, 0)))
        ), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertTrue(result.isValid());
    }

    @Test
    void objectAtAnotherTileThanOrigin() {
        MapPrototype structureMock = mockMapPrototype(Map.of(coordinatePrototype(0, 0), TilePrototype.notAccessible()));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(
            tileCoordinate(0, 0), new EmptyAccessibilityIndicator(),
            tileCoordinate(1, 0), new ColoredAccessibilityIndicator(null, new Source("tileName", tileCoordinate(3, 0)))
        ), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(1, 0), flattenedMap);

        assertFalse(result.isValid());
        assertThat(result.getError(), containsInAnyOrder("Tile (1,0) is not accessible, blocked by object tileName at (3,0)"));
    }

    @Test
    void objectWithYOffsetAtY0_isInvalid() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.notAccessible(),
            coordinatePrototype(0, 1), TilePrototype.notAccessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 1), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator()), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertFalse(result.isValid());
        assertThat(result.getError(), containsInAnyOrder("Object location must not result in negative coordinates, given: (0,0), origin is (0,1)"));
    }

    @Test
    void objectThatReachesOverTopAndRightBorderOfMapIsAllowed() {
        MapPrototype structureMock = mockMapPrototype(Map.of(
            coordinatePrototype(0, 0), TilePrototype.accessible(),
            coordinatePrototype(0, 1), TilePrototype.accessible(),
            coordinatePrototype(1, 0), TilePrototype.accessible(),
            coordinatePrototype(1, 1), TilePrototype.accessible()
        ));
        ConcreteMapObject object = new ConcreteMapObject("name", structureMock, coordinatePrototype(0, 0), null);
        var flattenedMap = new FlattenedMap("name", HashMap.of(tileCoordinate(0, 0), new EmptyAccessibilityIndicator()), someMapSource());

        Validation<List<String>, ConcreteMapObject> result = underTest.mayObjectBeInserted(object, tileCoordinate(0, 0), flattenedMap);

        assertTrue(result.isValid());
    }

    private MapSource someMapSource() {
        return new MapSource(new File(""));
    }
}