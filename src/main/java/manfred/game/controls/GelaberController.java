package manfred.game.controls;

import manfred.game.interact.gelaber.Gelaber;

import java.awt.event.KeyEvent;

public class GelaberController implements ControllerInterface {
    private KeyControls keyControls;
    private Gelaber gelaber;

    @Override
    public void keyPressed(KeyEvent event) {

    }

    @Override
    public void keyReleased(KeyEvent event) {

    }

    @Override
    public void setKeyControls(KeyControls keyControls) {
        this.keyControls = keyControls;
    }

    public void setGelaber(Gelaber gelaber) {
        this.gelaber = gelaber;
    }
}
