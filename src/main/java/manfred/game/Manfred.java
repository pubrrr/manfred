package manfred.game;

import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Manfred implements Paintable {
    private static final int SPEED = 2;

    private int x;
    private int y;

    private int dx = 0;
    private int dy = 0;

    private final int sizeX;
    private final int sizeY;

    public Manfred(int x, int y) {
        this.x = x;
        this.y = y;
        sizeX = GamePanel.PIXEL_BLOCK_SIZE;
        sizeY = GamePanel.PIXEL_BLOCK_SIZE;
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

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(getX(), getY(), sizeX, sizeY);
    }
}
