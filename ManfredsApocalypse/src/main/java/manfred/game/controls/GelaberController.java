package manfred.game.controls;

import manfred.game.interact.gelaber.Gelaber;
import org.springframework.lang.Nullable;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class GelaberController implements ControllerInterface {
    private Gelaber gelaber;

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public Consumer<KeyControls> keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                return gelaber.next();
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                gelaber.down();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                gelaber.up();
                break;
        }
        return KeyControls::doNothing;
    }

    public void setGelaber(Gelaber gelaber) {
        this.gelaber = gelaber;
    }

    @Override
    public void stop() {
        // do nothing
    }
}
