package manfred.manfreditor.map.view.mapobject;

import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class ObjectsGridCoordinateFactoryTest {

    @Test
    void emptyInput() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(0));

        assertThat(result, hasSize(0));
    }

    @Test
    void oneColumn_oneCoordinate() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(1));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(objectsGridCoordinate(0, 0)));
    }

    @Test
    void oneColumn_twoCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(2));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(objectsGridCoordinate(0, 0)));
        assertThat(result.get(1), is(objectsGridCoordinate(0, 1)));
    }

    @Test
    void oneColumn_threeCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(3));

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder(
            objectsGridCoordinate(0, 0),
            objectsGridCoordinate(0, 1),
            objectsGridCoordinate(0, 2)
        ));
    }

    @Test
    void twoColumns_twoCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(2));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(2));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(objectsGridCoordinate(0, 0)));
        assertThat(result.get(1), is(objectsGridCoordinate(1, 0)));
    }

    @Test
    void twoColumns_threeCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(2));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(3));

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder(
            objectsGridCoordinate(0, 0),
            objectsGridCoordinate(1, 0),
            objectsGridCoordinate(0, 1)
        ));
    }

    @Test
    void twoColumns_fiveCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(2));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(5));

        assertThat(result, hasSize(5));
        assertThat(result, containsInAnyOrder(
            objectsGridCoordinate(0, 0),
            objectsGridCoordinate(1, 0),
            objectsGridCoordinate(0, 1),
            objectsGridCoordinate(1, 1),
            objectsGridCoordinate(0, 2)
        ));
    }

    @Test
    void threeColumns_threeCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(3));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(3));

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder(
            objectsGridCoordinate(0, 0),
            objectsGridCoordinate(1, 0),
            objectsGridCoordinate(2, 0)
        ));
    }

    @Test
    void threeColumns_fourCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(3));

        List<ObjectsGridCoordinate> result = underTest.getCoordinates(PositiveInt.of(4));

        assertThat(result, hasSize(4));
        assertThat(result, containsInAnyOrder(
            objectsGridCoordinate(0, 0),
            objectsGridCoordinate(1, 0),
            objectsGridCoordinate(2, 0),
            objectsGridCoordinate(0, 1)
        ));
    }

    private ObjectsGridCoordinate objectsGridCoordinate(int x, int y) {
        return new ObjectsGridCoordinate(PositiveInt.of(x), PositiveInt.of(y));
    }
}