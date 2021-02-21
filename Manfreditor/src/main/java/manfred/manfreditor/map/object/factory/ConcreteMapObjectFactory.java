package manfred.manfreditor.map.object.factory;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.TileConversionAction;
import manfred.data.infrastructure.map.TileConversionRule;
import manfred.manfreditor.map.object.ConcreteMapObject;
import manfred.manfreditor.map.object.MapObject;

import java.util.Optional;

public class ConcreteMapObjectFactory implements TileConversionRule<MapObject> {

    @Override
    public Optional<TileConversionAction<MapObject>> applicableTo(MapPrototype input, MapPrototype.Coordinate coordinate) {
        return input.getFromMap(coordinate)
            .getTileObject()
            .map(validatedMapTileDto -> (ConcreteMapObject::new));
    }
}
