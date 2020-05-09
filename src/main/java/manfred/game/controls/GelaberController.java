package manfred.game.controls;

import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.Gelaber;

import java.awt.event.KeyEvent;

public class GelaberController implements ControllerInterface {
    private KeyControls keyControls;
    private Gelaber gelaber;
    private GamePanel panel;

    public GelaberController(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                boolean foundNext = gelaber.next();
                if (!foundNext) {
                    panel.deletePaintable(gelaber);
                    keyControls.controlManfred();
                }
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                gelaber.down();
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                gelaber.up();
                break;
        }
    }

    @Override
    public void setKeyControls(KeyControls keyControls) {
        this.keyControls = keyControls;
    }

    public void setGelaber(Gelaber gelaber) {
        this.gelaber = gelaber;
    }
}
