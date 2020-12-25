package manfred.game.graphics.paintable;

import manfred.game.interact.person.GelaberFacade;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class GelaberOverlay implements Paintable {

    private Paintable gelaber;

    public GelaberOverlay(Empty empty) {
        this.gelaber = empty;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        gelaber.paint(g, offset, x, y);
    }

    public void clear() {
        this.gelaber = new Empty();
    }

    public void setGelaber(GelaberFacade gelaberFacade) {
        this.gelaber = gelaberFacade;
    }
}
