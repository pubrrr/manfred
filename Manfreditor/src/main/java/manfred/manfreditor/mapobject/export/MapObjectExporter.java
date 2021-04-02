package manfred.manfreditor.mapobject.export;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.RawMapTileDtoReader;
import manfred.manfreditor.mapobject.NewMapObjectData;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapObjectExporter {

    private final MapObjectToDtoMapper mapObjectToDtoMapper;
    private final RawMapTileDtoReader rawMapTileDtoReader;

    public Try<Void> export(NewMapObjectData newMapObjectData) {
        return rawMapTileDtoReader.save(mapObjectToDtoMapper.map(newMapObjectData));
    }
}
