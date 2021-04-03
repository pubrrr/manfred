package manfred.manfreditor.map.model;

import lombok.Value;
import manfred.manfreditor.map.model.mapobject.MapObject;

@Value
public class LocatedMapObject {
    MapObject mapObject;
    Map.TileCoordinate location;
}
