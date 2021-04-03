package manfred.manfreditor.map.model;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.model.mapobject.None;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

class NewMapFactoryTest {

    private NewMapFactory underTest;

    @BeforeEach
    void setUp() {
        underTest = new NewMapFactory();
    }

    @Test
    void createEmptyMap() {
        Map result = underTest.create("name", PositiveInt.of(0), PositiveInt.of(0));

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(PositiveInt.of(0)));
        assertThat(result.getSizeY(), is(PositiveInt.of(0)));
    }

    @Test
    void create1x1Map() {
        Map result = underTest.create("name", PositiveInt.of(1), PositiveInt.of(1));

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(PositiveInt.of(1)));
        assertThat(result.getSizeY(), is(PositiveInt.of(1)));
        assertThat(result.getObjectAt(tileCoordinate(0, 0)), instanceOf(None.class));
    }

    @Test
    void create2x3Map() {
        Map result = underTest.create("name", PositiveInt.of(2), PositiveInt.of(3));

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(PositiveInt.of(2)));
        assertThat(result.getSizeY(), is(PositiveInt.of(3)));
        result.getObjects().forEach((tileCoordinate, mapObject) -> assertThat(mapObject, instanceOf(None.class)));
    }
}