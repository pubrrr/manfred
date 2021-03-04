package manfred.manfreditor.gui.view.mapobject;

import lombok.Value;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.gui.view.GridCoordinate;

@Value
public class ObjectsGridCoordinate implements GridCoordinate {
    PositiveInt x;
    PositiveInt y;
}
