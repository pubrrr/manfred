package manfred.game.graphics.paintable;

import lombok.Value;
import manfred.game.map.Map;

@Value
public class PaintableContainerElement {
    LocatedPaintable locatedPaintable;
    Map.Coordinate bottomLeftCoordinate;
}
