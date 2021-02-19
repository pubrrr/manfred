package manfred.manfreditor.map;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class MapConverterTest {

    private MapConverter underTest;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        underTest = new MapConverter();
    }

    @Test
    void convert() {
        MapPrototype input = new MapPrototype("name", mock(MapMatrix.class), List.of(), List.of(), List.of(), List.of());

        Map result = underTest.convert(input);

        assertThat(result.getName(), is("name"));
    }
}