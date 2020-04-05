package game;

import java.awt.event.KeyEvent;

public class Manfred {
    private static final int SPEED = 2;

    private int x;

    private int y;

    private int dx = 0;
    private int dy = 0;

    public Manfred(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                dx = -SPEED;
                break;
            case KeyEvent.VK_D:
                dx = SPEED;
                break;
            case KeyEvent.VK_S:
                dy = SPEED;
                break;
            case KeyEvent.VK_W:
                dy = -SPEED; // y-Achse ist invertiert: kleiner Werte werden weiter oben gezeichnet
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                dx = 0;
                break;
            case KeyEvent.VK_D:
                dx = 0;
                break;
            case KeyEvent.VK_S:
                dy = 0;
                break;
            case KeyEvent.VK_W:
                dy = 0;
                break;
        }
    }

    public void move() {
        x += dx;
        y += dy;
    }
}
