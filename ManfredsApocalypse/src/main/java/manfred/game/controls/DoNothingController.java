package manfred.game.controls;

import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

@Component
public class DoNothingController implements ControllerInterface {
    @Override
    public void keyPressed(KeyEvent event) {
        // do nothing
    }

    @Override
    public Consumer<KeyControls> keyReleased(KeyEvent event) {
        return KeyControls::doNothing;
    }

    @Override
    public void stop() {
        // do nothing
    }
}
