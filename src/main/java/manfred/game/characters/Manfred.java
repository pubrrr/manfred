package manfred.game.characters;

import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;

public class Manfred implements Paintable {
    private static final int SPEED = 2;

    private int x;
    private int y;

    private int currentSpeedX = 0;
    private int currentSpeedY = 0;
    private boolean movesLeft = false;
    private boolean movesRight = false;
    private boolean movesUp = false;
    private boolean movesDown = false;

    private final int sizeX;
    private final int sizeY;

    private MapCollider collider;

    public Manfred(int x, int y, MapCollider collider) {
        this.x = x;
        this.y = y;
        sizeX = GamePanel.PIXEL_BLOCK_SIZE;
        sizeY = GamePanel.PIXEL_BLOCK_SIZE;
        this.collider = collider;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void left() {
        if (!movesLeft) {
            movesLeft = true;
            currentSpeedX -= SPEED;
        }
    }

    public void right() {
        if (!movesRight) {
            movesRight = true;
            currentSpeedX += SPEED;
        }
    }

    public void up() {
        if (!movesUp) {
            movesUp = true;
            // y-Achse ist invertiert: kleiner Werte werden weiter oben gezeichnet
            currentSpeedY -= SPEED;
        }
    }

    public void down() {
        if (!movesDown) {
            movesDown = true;
            currentSpeedY += SPEED;
        }
    }

    public void stopX() {
        movesRight = false;
        movesLeft = false;
        currentSpeedX = 0;
    }

    public void stopY() {
        movesUp = false;
        movesDown = false;
        currentSpeedY = 0;
    }

    public void move() {
        if (!collider.collides(
                x + currentSpeedX,
                x + currentSpeedX + (sizeX - 1),
                y,
                y + (sizeY - 1)
        )) {
            x += currentSpeedX;
        }

        if (!collider.collides(
                x,
                x + (sizeX - 1),
                y + currentSpeedY,
                y + currentSpeedY + (sizeY - 1)
        )) {
            y += currentSpeedY;
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(getX(), getY(), sizeX, sizeY);
    }
}
