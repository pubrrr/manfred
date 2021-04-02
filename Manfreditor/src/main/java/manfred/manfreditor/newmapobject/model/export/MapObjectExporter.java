package manfred.manfreditor.newmapobject.model.export;

import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.RawMapTileDtoReader;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class MapObjectExporter {

    private final MapObjectToDtoMapper mapObjectToDtoMapper;
    private final RawMapTileDtoReader rawMapTileDtoReader;

    public Try<List<File>> export(NewMapObjectData newMapObjectData) {
        return rawMapTileDtoReader.save(mapObjectToDtoMapper.map(newMapObjectData));
    }
}
