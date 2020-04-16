package manfred.game.controls;

import java.awt.event.KeyEvent;

public interface ControllerInterface {
    void keyPressed(KeyEvent event);

    void keyReleased(KeyEvent event);

    void setKeyControls(KeyControls keyControls);
}