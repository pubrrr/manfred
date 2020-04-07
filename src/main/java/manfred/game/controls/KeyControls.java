package manfred.game.controls;

import manfred.game.Manfred;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControls implements KeyListener {
    private Manfred manfred;

    public KeyControls(Manfred manfred) {
        this.manfred = manfred;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        manfred.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        manfred.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
