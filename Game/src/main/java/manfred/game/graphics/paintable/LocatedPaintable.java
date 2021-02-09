package manfred.game.graphics.paintable;

import manfred.game.graphics.PanelCoordinate;

import java.awt.*;

public interface LocatedPaintable {
    void paint(Graphics g, PanelCoordinate coordinate);
}
