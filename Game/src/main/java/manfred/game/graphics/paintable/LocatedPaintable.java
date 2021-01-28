package manfred.game.graphics.paintable;

import java.awt.*;

public interface LocatedPaintable {
    void paint(Graphics g, Point offset, Integer x, Integer y);
}
