package manfred.manfreditor.map.model;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.MapReader;
import manfred.data.persistence.reader.MapSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        when(mapReaderMock.load(any(MapSource.class))).thenReturn(new MapPrototype("", null, List.of(), List.of(), List.of(), null, null));
        when(mapConverterMock.convert(any())).thenReturn(mapMock);

        Map result = underTest.provide("name");

        assertThat(result, is(mapMock));
    }
}