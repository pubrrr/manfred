package manfred.manfreditor.map.accessibility;

import lombok.Value;
import manfred.manfreditor.map.Map;

@Value
public class Source {
    String tileName;
    Map.TileCoordinate tileCoordinate;
}
