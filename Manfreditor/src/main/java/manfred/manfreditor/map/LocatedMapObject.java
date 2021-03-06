package manfred.manfreditor.map;

import lombok.Value;
import manfred.manfreditor.mapobject.MapObject;

@Value
public class LocatedMapObject {
    MapObject mapObject;
    Map.TileCoordinate location;
}
