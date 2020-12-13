package manfred.game.controls;

import manfred.game.attack.Caster;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;

@Component
public class ManfredController implements ControllerInterface {
    private final Manfred manfred;
    private final Caster attackCaster;

    public ManfredController(Manfred manfred, Caster attackCaster) {
        this.manfred = manfred;
        this.attackCaster = attackCaster;
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
                attackCaster.cast(manfred.getSprite(), manfred.getDirection());
                return;
            default:
                CombinationElement.fromKeyEvent(event).ifPresent(attackCaster::addToCombination);
        }
    }

    @Override
    @Nullable
    public Consumer<KeyControls> keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                manfred.stopX();
                manfred.checkForVerticalViewDirection();
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_W:
                manfred.stopY();
                manfred.checkForHorizontalViewDirection();
                break;
            case KeyEvent.VK_ENTER:
                Consumer<KeyControls> interact = manfred.interact();
                if (interact != null) {
                    attackCaster.off();
                }
                return interact;
        }
        return null;
    }

    @Override
    public void stop() {
        manfred.stopX();
        manfred.stopY();
        attackCaster.off();
    }
}
