package manfred.game.controls;

import manfred.game.interact.gelaber.Gelaber;

import java.awt.event.KeyEvent;

public class GelaberController implements ControllerInterface {
    private final ControllerInterface controller;
    private Gelaber gelaber;

    public GelaberController(ControllerInterface controller, Gelaber gelaber) {
        this.controller = controller;
        this.gelaber = gelaber;
    }

    @Override
    public ControllerInterface keyPressed(KeyEvent event) {
        return this;
    }

    @Override
    public ControllerInterface keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
//                return gelaber.next();
                return this;
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

    public void setGelaber(Gelaber gelaber) {
        this.gelaber = gelaber;
    }

    @Override
    public void stop() {
        // do nothing
    }

    @Override
    public ControllerInterface move() {
        return this;
    }
}
