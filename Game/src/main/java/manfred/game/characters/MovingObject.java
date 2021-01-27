package manfred.game.characters;

import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class MovingObject implements Paintable {
    protected Sprite sprite;
    protected final int speed;

    protected boolean movesLeft = false;
    protected boolean movesRight = false;
    protected boolean movesUp = false;
    protected boolean movesDown = false;
    protected Direction viewDirection = Direction.DOWN;

    protected int currentSpeedX = 0;
    protected int currentSpeedY = 0;

    protected MovingObject(int speed, int x, int y, int width, int spriteHeight, int baseHeight, BufferedImage image) {
        this.speed = speed;
        this.sprite = new Sprite(x, y, width, spriteHeight, baseHeight, image);
    }

    public int getX() {
        return this.sprite.x;
    }

    public int getY() {
        return this.sprite.y + sprite.getSpriteHeight() - sprite.getBaseHeight();
    }

    public void left() {
        if (!movesLeft) {
            viewDirection = Direction.LEFT;
            movesLeft = true;
            currentSpeedX -= speed;
        }
    }

    public void right() {
        if (!movesRight) {
            viewDirection = Direction.RIGHT;
            movesRight = true;
            currentSpeedX += speed;
        }
    }

    public void up() {
        if (!movesUp) {
            viewDirection = Direction.UP;
            movesUp = true;
            // y-Achse ist invertiert: kleiner Werte werden weiter oben gezeichnet
            currentSpeedY -= speed;
        }
    }

    public void down() {
        if (!movesDown) {
            viewDirection = Direction.DOWN;
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

    public void checkCollisionsAndMove(MapCollider mapCollider) {
        if (!collidesVertically(mapCollider)) {
            this.sprite.translate(currentSpeedX, 0);
        }

        if (!collidesHorizontally(mapCollider)) {
            this.sprite.translate(0, currentSpeedY);
        }
    }

    protected boolean collidesVertically(MapCollider mapCollider) {
        return mapCollider.collides(
                this.sprite.getLeft() + currentSpeedX,
                this.sprite.getRight() - 1 + currentSpeedX,
                this.sprite.getBaseTop(),
                this.sprite.getBottom() - 1
        );
    }

    protected boolean collidesHorizontally(MapCollider mapCollider) {
        return mapCollider.collides(
                this.sprite.getLeft(),
                this.sprite.getRight() - 1,
                this.sprite.getBaseTop() + currentSpeedY,
                this.sprite.getBottom() - 1 + currentSpeedY
        );
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Direction getDirection() {
        return this.viewDirection;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        this.sprite.paint(g, offset, x, y);
    }

    public void checkForVerticalViewDirection() {
        if (currentSpeedY > 0) {
            viewDirection = Direction.DOWN;
        } else if (currentSpeedY < 0) {
            viewDirection = Direction.UP;
        }
    }

    public void checkForHorizontalViewDirection() {
        if (currentSpeedX > 0) {
            viewDirection = Direction.RIGHT;
        } else if (currentSpeedX < 0) {
            viewDirection = Direction.LEFT;
        }
    }
}

