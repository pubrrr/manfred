package manfred.manfreditor.map.objectfactory;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.mapobject.MapObject;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConcreteMapObjectFactory implements TileConversionRule<MapObject> {

    private final MapObjectRepository mapObjectRepository;

    public ConcreteMapObjectFactory(MapObjectRepository mapObjectRepository) {
        this.mapObjectRepository = mapObjectRepository;
    }

    @Override
    public Optional<TileConversionAction<MapObject>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return input.getFromMap(coordinate)
            .getTileObject()
            .map(validatedMapTileDto -> (() -> mapObjectRepository.getOrCreate(validatedMapTileDto)));
    }
}
