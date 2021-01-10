package manfred.game.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControls implements KeyListener {
    private ControllerInterface activeController;

    public KeyControls(ControllerInterface activeController) {
        this.activeController = activeController;
    }

    @Override
    public synchronized void keyPressed(KeyEvent event) {
        activeController = activeController.keyPressed(event);
    }

    @Override
    public synchronized void keyReleased(KeyEvent event) {
        activeController = activeController.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public synchronized void move() {
        activeController = activeController.move();
    }
}
