package manfred.game.graphics.paintable;

import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class Empty implements Paintable {
    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        // paint nothing
    }
}
