package manfred.manfreditor.map.model.export;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import io.vavr.control.Try;
import manfred.data.persistence.PreviousFileContent;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.reader.RawMapReader;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapExporterTest {

    private MapExporter underTest;
    private RawMapReader rawMapReaderMock;
    private MapToDtoMapper mapToDtoMapperMock;

    @BeforeEach
    void setUp() {
        rawMapReaderMock = mock(RawMapReader.class);
        mapToDtoMapperMock = mock(MapToDtoMapper.class);
        underTest = new MapExporter(rawMapReaderMock, mapToDtoMapperMock);
    }

    @Test
    void export() {
        Try<Option<PreviousFileContent>> expected = Try.success(Option.none());
        var input = new FlattenedMap("name", HashMap.empty());
        when(rawMapReaderMock.save(any(), any())).thenReturn(expected);
        when(mapToDtoMapperMock.map(any())).thenReturn(mock(RawMapDto.class));

        Try<Option<PreviousFileContent>> result = underTest.export(input, mock(File.class));

        assertThat(result, is(expected));
    }
}