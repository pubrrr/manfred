package manfred.manfreditor.map;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapConverterTest {

    private MapConverter underTest;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        underTest = new MapConverter();
    }

    @Test
    void convertEmptyOneTileMap() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getCoordinateSet()).thenReturn(List.of(coordinate(0, 0)));
        when(input.getName()).thenReturn("name");

        Map result = underTest.convert(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(1));
        assertThat(result.getSizeY(), is(1));
    }

    @Test
    void convertEmptyMultipleTilesMap() {
        MapPrototype input = mock(MapPrototype.class);
        when(input.getCoordinateSet()).thenReturn(List.of(
            coordinate(0, 0),
            coordinate(1, 0),
            coordinate(2, 0),
            coordinate(0, 1),
            coordinate(1, 1),
            coordinate(2, 1)
        ));
        when(input.getName()).thenReturn("name");

        Map result = underTest.convert(input);

        assertThat(result.getName(), is("name"));
        assertThat(result.getSizeX(), is(3));
        assertThat(result.getSizeY(), is(2));
    }

    private MapPrototype.Coordinate coordinate(int x, int y) {
        return new CoordinateDouble(x, y);
    }

    private static class CoordinateDouble extends MapPrototype.Coordinate {
        public CoordinateDouble(int x, int y) {
            super(PositiveInt.of(x), PositiveInt.of(y));
        }
    }
}