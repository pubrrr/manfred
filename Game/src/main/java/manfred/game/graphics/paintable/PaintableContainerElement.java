package manfred.game.graphics.paintable;

import lombok.Value;

@Value
public class PaintableContainerElement {
    LocatedPaintable locatedPaintable;
    int x;
    int y;
}
