package manfred.game.controls;

import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.gelaber.Gelaber;

import java.awt.event.KeyEvent;

public class GelaberController implements ControllerInterface {
    private final ControllerInterface previous;
    private final Gelaber gelaber;
    private final GelaberOverlay gelaberOverlay;

    public GelaberController(ControllerInterface previous, Gelaber gelaber, GelaberOverlay gelaberOverlay) {
        this.previous = previous;
        this.gelaber = gelaber;
        this.gelaberOverlay = gelaberOverlay;
    }

    @Override
    public ControllerInterface keyPressed(KeyEvent event) {
        return this;
    }

    @Override
    public ControllerInterface keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return gelaber.next().apply(this);
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                gelaber.down();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                gelaber.up();
                break;
        }
        return this;
    }

    @Override
    public void stop() {
        // do nothing
    }

    @Override
    public ControllerInterface move() {
        return this;
    }

    public ControllerInterface previous() {
        gelaberOverlay.clear();
        return this.previous;
    }
}
