package manfred.game.controls;

import manfred.game.characters.Manfred;
import org.springframework.lang.Nullable;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class ManfredController implements ControllerInterface {
    private Manfred manfred;

    public ManfredController(Manfred manfred) {
        this.manfred = manfred;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
                manfred.left();
                break;
            case KeyEvent.VK_D:
                manfred.right();
                break;
            case KeyEvent.VK_S:
                manfred.down();
                break;
            case KeyEvent.VK_W:
                manfred.up();
                break;
        }
    }

    @Override
    @Nullable
    public Consumer<KeyControls> keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                manfred.stopX();
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_W:
                manfred.stopY();
                break;
            case KeyEvent.VK_ENTER:
                return manfred.interact();
        }
        return null;
    }
}
