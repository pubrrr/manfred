package manfred.manfreditor.map.export;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.persistence.PreviousFileContent;
import manfred.data.persistence.reader.RawMapReader;
import manfred.manfreditor.map.flattened.FlattenedMap;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@AllArgsConstructor
public class MapExporter {

    private final RawMapReader rawMapReader;
    private final MapToDtoMapper mapToDtoMapper;

    public Try<Option<PreviousFileContent>> export(FlattenedMap flattenedMap, URL targetUrl) {
        return rawMapReader.save(mapToDtoMapper.map(flattenedMap), targetUrl);
    }
}
