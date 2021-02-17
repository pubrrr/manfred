package manfred.game.graphics.paintable;

import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;

public interface LocatedPaintable {
    void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate);
}
