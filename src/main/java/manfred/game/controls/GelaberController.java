package manfred.game.controls;

import java.awt.event.KeyEvent;

public class GelaberController implements ControllerInterface {
    private KeyControls keyControls;

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
}
