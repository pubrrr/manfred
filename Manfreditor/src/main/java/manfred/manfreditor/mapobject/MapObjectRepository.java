package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import org.springframework.stereotype.Component;

@Component
public class MapObjectRepository {

    public ConcreteMapObject getOrCreate(ValidatedMapTileDto validatedMapTileDto) {
        return new ConcreteMapObject(validatedMapTileDto.getName());
    }
}
