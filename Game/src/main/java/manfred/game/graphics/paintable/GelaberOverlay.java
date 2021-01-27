package manfred.game.graphics.paintable;

import manfred.game.interact.person.gelaber.GelaberFacade;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class GelaberOverlay {
    public static final Paintable EMPTY = (graphics) -> {/* paint nothing */};

    private Paintable gelaber = EMPTY;

    public void paint(Graphics g) {
        gelaber.paint(g);
    }

    public void clear() {
        this.gelaber = EMPTY;
    }

    public void setGelaber(GelaberFacade gelaberFacade) {
        this.gelaber = gelaberFacade;
    }
}
