package manfred.manfreditor.mapobject;

import io.vavr.collection.Map;
import io.vavr.control.Either;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.accessibility.Source;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CoordinateHelper.coordinatePrototype;
import static manfred.manfreditor.helper.CoordinateHelper.mockMapPrototype;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class ConcreteMapObjectTest {

    @Test
    void getStructureAt_withOffset() {
        TilePrototype tileMock_0_0 = TilePrototype.notAccessible();
        TilePrototype tileMock_1_0 = TilePrototype.accessible();
        TilePrototype tileMock_0_1 = TilePrototype.notAccessible();
        TilePrototype tileMock_1_1 = TilePrototype.accessible();

        MapPrototype mapPrototypeMock = mockMapPrototype(java.util.Map.of(
            coordinatePrototype(0, 0), tileMock_0_0,
            coordinatePrototype(1, 0), tileMock_1_0,
            coordinatePrototype(0, 1), tileMock_0_1,
            coordinatePrototype(1, 1), tileMock_1_1
        ));

        var underTest = new ConcreteMapObject("name", mapPrototypeMock, coordinatePrototype(0, 1), null);
        Either<String, Map<TileCoordinate, AccessibilityIndicator>> structure = underTest.getStructureAt(tileCoordinate(1, 1));

        AccessibilityIndicator indicator_1_0 = structure.get().get(tileCoordinate(1, 0)).get();
        assertThat(indicator_1_0, instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(indicator_1_0.getSource().isPresent(), is(true));
        assertThat(indicator_1_0.getSource().get(), is(new Source("name", tileCoordinate(1, 1))));

        AccessibilityIndicator indicator_1_1 = structure.get().get(tileCoordinate(1, 1)).get();
        assertThat(indicator_1_1, instanceOf(ColoredAccessibilityIndicator.class));
        assertThat(indicator_1_1.getSource().isPresent(), is(true));
        assertThat(indicator_1_1.getSource().get(), is(new Source("name", tileCoordinate(1, 1))));

        assertThat(structure.get().get(tileCoordinate(2, 0)).get(), instanceOf(EmptyAccessibilityIndicator.class));
        assertThat(structure.get().get(tileCoordinate(2, 1)).get(), instanceOf(EmptyAccessibilityIndicator.class));
    }

    @Test
    void getStructureAt_fails() {
        var underTest = new ConcreteMapObject("name", null, coordinatePrototype(0, 1), null);
        Either<String, Map<TileCoordinate, AccessibilityIndicator>> structure = underTest.getStructureAt(tileCoordinate(0, 0));

        assertThat(structure.isLeft(), is(true));
        assertThat(structure.getLeft(), is("Object location must not result in negative coordinates, given: (0,0), origin is (0,1)"));
    }
}
