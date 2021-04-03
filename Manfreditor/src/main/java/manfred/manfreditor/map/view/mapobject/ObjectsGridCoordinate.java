package manfred.manfreditor.map.view.mapobject;

import lombok.Value;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.view.GridCoordinate;

@Value
public class ObjectsGridCoordinate implements GridCoordinate {
    PositiveInt x;
    PositiveInt y;
}
