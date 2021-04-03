package manfred.manfreditor.map.model.export;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.persistence.PreviousFileContent;
import manfred.data.persistence.reader.RawMapReader;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class MapExporter {

    private final RawMapReader rawMapReader;
    private final MapToDtoMapper mapToDtoMapper;

    public Try<Option<PreviousFileContent>> export(FlattenedMap flattenedMap, File fileToSaveIn) {
        return rawMapReader.save(mapToDtoMapper.map(flattenedMap), fileToSaveIn);
    }
}
