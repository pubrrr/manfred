package manfred.game.controls;

import manfred.game.characters.Manfred;
import org.springframework.lang.Nullable;

import java.awt.event.KeyEvent;
import java.util.Stack;
import java.util.function.Consumer;

public class ManfredController implements ControllerInterface {
    private Manfred manfred;

    private boolean castMode = false;
    private Stack<String> attackCombination = new Stack<>();

    public ManfredController(Manfred manfred) {
        this.manfred = manfred;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
                manfred.left();
                return;
            case KeyEvent.VK_D:
                manfred.right();
                return;
            case KeyEvent.VK_S:
                manfred.down();
                return;
            case KeyEvent.VK_W:
                manfred.up();
                return;
            case KeyEvent.VK_SPACE:
                if (castMode) {
                    manfred.cast(this.attackCombination);
                } else {
                    castMode = true;
                }
                return;
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
