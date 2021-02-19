package manfred.manfreditor.map;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.MapReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapProviderTest {

    private MapReader mapReaderMock;
    private MapConverter mapConverterMock;
    private MapProvider underTest;

    @BeforeEach
    void setUp() {
        mapReaderMock = mock(MapReader.class);
        mapConverterMock = mock(MapConverter.class);

        underTest = new MapProvider(mapReaderMock, mapConverterMock);
    }

    @Test
    void provide() throws InvalidInputException {
        Map mapMock = mock(Map.class);
        when(mapReaderMock.load(any())).thenReturn(new MapPrototype("", null, null, null, null, null));
        when(mapConverterMock.convert(any())).thenReturn(mapMock);

        Map result = underTest.provide("name");

        assertThat(result, is(mapMock));
    }
}