package manfred.game.characters;

import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.util.function.Consumer;

abstract public class MovingObject {
    protected Sprite sprite;
    protected final int speed;

    protected boolean movesLeft = false;
    protected boolean movesRight = false;
    protected boolean movesUp = false;
    protected boolean movesDown = false;
    protected Direction viewDirection = Direction.down;

    protected MapCollider collider;

    protected int currentSpeedX = 0;
    protected int currentSpeedY = 0;

    protected MovingObject(int speed, int x, int y, int sizeX, int sizeY, MapCollider collider) {
        this.speed = speed;
        this.sprite = new Sprite(x, y, sizeX, sizeY);
        this.collider = collider;
    }

    public int getX() {
        return this.sprite.x;
    }

    public int getY() {
        return this.sprite.y;
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
        if (!collidesVertically()) {
            this.sprite.translate(currentSpeedX, 0);
        }

        if (!collidesHorizontally()) {
            this.sprite.translate(0, currentSpeedY);
        }
        return null;
    }

    protected boolean collidesVertically() {
        return collider.collides(
                this.sprite.left() + currentSpeedX,
                this.sprite.right() - 1 + currentSpeedX,
                this.sprite.top(),
                this.sprite.bottom() - 1
        );
    }

    protected boolean collidesHorizontally() {
        return collider.collides(
                this.sprite.left(),
                this.sprite.right() - 1,
                this.sprite.top() + currentSpeedY,
                this.sprite.bottom() - 1 + currentSpeedY
        );
    }

    public boolean intersectsSprite(Sprite otherSprite) {
        return this.sprite.intersects(otherSprite);
    }
}
