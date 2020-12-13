package manfred.game.controls;

import org.springframework.lang.Nullable;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public interface ControllerInterface {
    void keyPressed(KeyEvent event);

    @Nullable
    Consumer<KeyControls> keyReleased(KeyEvent event);

    void stop();
}