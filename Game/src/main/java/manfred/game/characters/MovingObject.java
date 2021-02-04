package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.graphics.paintable.LocatedPaintable;

abstract public class MovingObject implements LocatedPaintable {
    protected Sprite sprite;
    protected Velocity velocity;

    protected boolean movesLeft = false;
    protected boolean movesRight = false;
    protected boolean movesUp = false;
    protected boolean movesDown = false;
    protected Direction viewDirection = Direction.DOWN;

    protected MovingObject(Velocity velocity, int x, int y, PositiveInt width, PositiveInt spriteHeight, PositiveInt baseHeight) {
        this.velocity = velocity;
        this.sprite = new Sprite(x, y, width.value(), spriteHeight.value(), baseHeight.value());
    }

    public int getX() {
        return this.sprite.x;
    }

    public int getY() {
        return this.sprite.y + sprite.getSpriteHeight() - sprite.getBaseHeight();
    }

    public void left() {
        if (!movesLeft) {
            accelerate(Direction.LEFT);
            movesLeft = true;
        }
    }

    public void right() {
        if (!movesRight) {
            accelerate(Direction.RIGHT);
            movesRight = true;
        }
    }

    public void up() {
        if (!movesUp) {
            accelerate(Direction.UP);
            movesUp = true;
        }
    }

    public void down() {
        if (!movesDown) {
            accelerate(Direction.DOWN);
            movesDown = true;
        }
    }

    private void accelerate(Direction direction) {
        this.viewDirection = direction;
        this.velocity = this.velocity.accelerate(direction);
    }

    public void stopX() {
        movesRight = false;
        movesLeft = false;
        this.velocity = velocity.stopX();
    }

    public void stopY() {
        movesUp = false;
        movesDown = false;
        this.velocity = velocity.stopY();
    }

    public void checkCollisionsAndMove(MapCollider mapCollider) {
        if (!collidesVertically(mapCollider)) {
            this.sprite.translate(this.velocity.getVector().x(), 0);
        }

        if (!collidesHorizontally(mapCollider)) {
            this.sprite.translate(0, this.velocity.getVector().y());
        }
    }

    protected boolean collidesVertically(MapCollider mapCollider) {
        return mapCollider.collides(
                this.sprite.getLeft() + this.velocity.getVector().x(),
                this.sprite.getRight() - 1 + this.velocity.getVector().x(),
                this.sprite.getBaseTop(),
                this.sprite.getBottom() - 1
        );
    }

    protected boolean collidesHorizontally(MapCollider mapCollider) {
        return mapCollider.collides(
                this.sprite.getLeft(),
                this.sprite.getRight() - 1,
                this.sprite.getBaseTop() + this.velocity.getVector().y(),
                this.sprite.getBottom() - 1 + this.velocity.getVector().y()
        );
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Direction getDirection() {
        return this.viewDirection;
    }

    public void checkForVerticalViewDirection() {
        if (this.velocity.getVector().y() > 0) {
            viewDirection = Direction.DOWN;
        } else if (this.velocity.getVector().y() < 0) {
            viewDirection = Direction.UP;
        }
    }

    public void checkForHorizontalViewDirection() {
        if (this.velocity.getVector().x() > 0) {
            viewDirection = Direction.RIGHT;
        } else if (this.velocity.getVector().x() < 0) {
            viewDirection = Direction.LEFT;
        }
    }
}

