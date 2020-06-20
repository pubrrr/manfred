package manfred.game.characters;

import manfred.game.controls.KeyControls;
import manfred.game.graphics.Paintable;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

abstract public class MovingObject implements Paintable {
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

    protected MovingObject(int speed, int x, int y, int width, int spriteHeight, int baseHeight, BufferedImage image, MapCollider collider) {
        this.speed = speed;
        this.sprite = new Sprite(x, y, width, spriteHeight, baseHeight, image);
        this.collider = collider;
    }

    public int getX() {
        return this.sprite.x;
    }

    public int getY() {
        return this.sprite.y + sprite.getSpriteHeight() - sprite.getBaseHeight();
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
                this.sprite.getLeft() + currentSpeedX,
                this.sprite.getRight() - 1 + currentSpeedX,
                this.sprite.getBaseTop(),
                this.sprite.getBottom() - 1
        );
    }

    protected boolean collidesHorizontally() {
        return collider.collides(
                this.sprite.getLeft(),
                this.sprite.getRight() - 1,
                this.sprite.getBaseTop() + currentSpeedY,
                this.sprite.getBottom() - 1 + currentSpeedY
        );
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        this.sprite.paint(g, offset, x, y);
    }

    public void checkForVerticalViewDirection() {
        if (currentSpeedY > 0) {
            viewDirection = Direction.down;
        } else if (currentSpeedY < 0) {
            viewDirection = Direction.up;
        }
    }

    public void checkForHorizontalViewDirection() {
        if (currentSpeedX > 0) {
            viewDirection = Direction.right;
        } else if (currentSpeedX < 0) {
            viewDirection = Direction.left;
        }
    }
}

