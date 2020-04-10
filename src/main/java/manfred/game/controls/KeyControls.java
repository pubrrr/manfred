package manfred.game.controls;

import manfred.game.characters.Manfred;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyControls implements KeyListener {
    private Manfred manfred;

    public KeyControls(Manfred manfred) {
        this.manfred = manfred;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_A:
                System.out.println("a");
                manfred.left();
                break;
            case KeyEvent.VK_D:
                System.out.println("d");
                manfred.right();
                break;
            case KeyEvent.VK_S:
                System.out.println("s");
                manfred.down();
                break;
            case KeyEvent.VK_W:
                System.out.println("w");
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
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
