package manfred.manfreditor.gui.view.mapobject;

import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

class ObjectsViewCoordinateFactoryTest {

    @Test
    void emptyInput() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(0));

        assertThat(result, hasSize(0));
    }

    @Test
    void oneColumn_oneCoordinate() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(1));

        assertThat(result, hasSize(1));
        assertThat(result.get(0), is(new ObjectsViewCoordinate(0, 0)));
    }

    @Test
    void oneColumn_twoCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(2));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(new ObjectsViewCoordinate(0, 0)));
        assertThat(result.get(1), is(new ObjectsViewCoordinate(0, 1)));
    }

    @Test
    void oneColumn_threeCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(1));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(3));

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(0, 1),
            new ObjectsViewCoordinate(0, 2)
        ));
    }

    @Test
    void twoColumns_twoCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(2));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(2));

        assertThat(result, hasSize(2));
        assertThat(result.get(0), is(new ObjectsViewCoordinate(0, 0)));
        assertThat(result.get(1), is(new ObjectsViewCoordinate(1, 0)));
    }

    @Test
    void twoColumns_threeCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(2));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(3));

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0),
            new ObjectsViewCoordinate(0, 1)
        ));
    }

    @Test
    void twoColumns_fiveCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(2));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(5));

        assertThat(result, hasSize(5));
        assertThat(result, containsInAnyOrder(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0),
            new ObjectsViewCoordinate(0, 1),
            new ObjectsViewCoordinate(1, 1),
            new ObjectsViewCoordinate(0, 2)
        ));
    }

    @Test
    void threeColumns_threeCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(3));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(3));

        assertThat(result, hasSize(3));
        assertThat(result, containsInAnyOrder(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0),
            new ObjectsViewCoordinate(2, 0)
        ));
    }

    @Test
    void threeColumns_fourCoordinates() {
        ObjectsViewCoordinateFactory underTest = new ObjectsViewCoordinateFactory(PositiveInt.ofNonZero(3));

        List<ObjectsViewCoordinate> result = underTest.getCoordinates(PositiveInt.of(4));

        assertThat(result, hasSize(4));
        assertThat(result, containsInAnyOrder(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0),
            new ObjectsViewCoordinate(2, 0),
            new ObjectsViewCoordinate(0, 1)
        ));
    }
}