package manfred.game.graphics.paintable;

import java.awt.*;

public interface Paintable {
    void paint(Graphics g, Point offset, Integer x, Integer y);
}
