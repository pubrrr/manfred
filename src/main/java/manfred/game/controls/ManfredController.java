package manfred.game.controls;

import manfred.game.characters.Manfred;
import manfred.game.interact.Person;

import java.awt.event.KeyEvent;

public class ManfredController implements ControllerInterface {
    private Manfred manfred;

    private KeyControls keyControls;

    public ManfredController(Manfred manfred) {
        this.manfred = manfred;
    }

    public void setKeyControls(KeyControls keyControls) {
        this.keyControls = keyControls;
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
    public void keyReleased(KeyEvent event) {
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
                Person person = (Person) manfred.getInteract();
                if (person != null) {
                    keyControls.controlGelaber(person.getGelaber());
                }
                break;
        }
    }
}