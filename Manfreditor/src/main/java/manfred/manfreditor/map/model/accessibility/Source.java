package manfred.manfreditor.map.model.accessibility;

import lombok.Value;
import manfred.manfreditor.map.model.Map;

@Value
public class Source {
    String tileName;
    Map.TileCoordinate tileCoordinate;
}
