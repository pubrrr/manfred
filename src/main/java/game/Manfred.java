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
        dx = SPEED;
    }

    public void keyReleased(KeyEvent e) {
        dx = 0;
    }

    public void move() {
        x += dx;
        y += dy;
    }
}
