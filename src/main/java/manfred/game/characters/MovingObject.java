package manfred.game.characters;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

abstract public class MovingObject {
    protected int x;
    protected int y;
    protected final int speed;
    private int healthPoints;

    protected boolean movesLeft = false;
    protected boolean movesRight = false;
    protected boolean movesUp = false;
    protected boolean movesDown = false;
    protected Direction viewDirection = Direction.down;

    protected final int sizeX;
    protected final int sizeY;

    protected MapCollider collider;

    protected int currentSpeedX = 0;
    protected int currentSpeedY = 0;

    protected MovingObject(int speed, int x, int y, int sizeX, int sizeY, int healthPoints, MapCollider collider) {
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.healthPoints = healthPoints;
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
            viewDirection = Direction.left;
            movesLeft = true;
            currentSpeedX -= speed;
        }
    }

    public void right() {
        if (!movesRight) {
            viewDirection = Direction.right;
            movesRight = true;
            currentSpeedX += speed;
        }
    }

    public void up() {
        if (!movesUp) {
            viewDirection = Direction.up;
            movesUp = true;
            // y-Achse ist invertiert: kleiner Werte werden weiter oben gezeichnet
            currentSpeedY -= speed;
        }
    }

    public void down() {
        if (!movesDown) {
            viewDirection = Direction.down;
            movesDown = true;
            currentSpeedY += speed;
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

    @Nullable
    public Consumer<KeyControls> move() {
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
        return null;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
}
