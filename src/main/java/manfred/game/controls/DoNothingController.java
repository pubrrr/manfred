package manfred.game.controls;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class DoNothingController implements ControllerInterface {
    @Override
    public void keyPressed(KeyEvent event) {
        // do nothing
    }

    @Override
    public Consumer<KeyControls> keyReleased(KeyEvent event) {
        // do nothing
        return null;
    }
}
