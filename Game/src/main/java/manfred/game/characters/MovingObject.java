package manfred.game.characters;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.sprite.Sprite;
import manfred.game.geometry.Rectangle;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.map.CollisionDetector;
import manfred.game.map.Map;

import java.awt.*;

abstract public class MovingObject<SPRITE extends Sprite> implements LocatedPaintable {
    protected Rectangle baseObject;
    protected SPRITE sprite;
    protected Velocity velocity;

    protected boolean movesLeft = false;
    protected boolean movesRight = false;
    protected boolean movesUp = false;
    protected boolean movesDown = false;
    protected Direction viewDirection = Direction.DOWN;

    protected MovingObject(Velocity velocity, Map.Coordinate initialBottomLeft, PositiveInt objectWidth, PositiveInt objectDepth, SPRITE sprite) {
        this.velocity = velocity;
        this.baseObject = new Rectangle(initialBottomLeft, objectWidth, objectDepth);
        this.sprite = sprite;
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

    @Override
    public void paint(Graphics g, PanelCoordinate coordinate) {
        g.drawImage(
            sprite.getImage(),
            coordinate.getX(),
            coordinate.getY(),
            sprite.getWidth().value(),
            sprite.getHeight().value(),
            null
        );
    }

    public void checkCollisionsAndMove(CollisionDetector collisionDetector) {
        Rectangle verticallyMoved = this.baseObject.translate(this.velocity.getVector().projectOnXAxis());
        this.baseObject = moveToIfAccessible(verticallyMoved, collisionDetector);

        Rectangle horizontallyMoved = this.baseObject.translate(this.velocity.getVector().projectOnYAxis());
        this.baseObject = moveToIfAccessible(horizontallyMoved, collisionDetector);
    }

    protected Rectangle moveToIfAccessible(Rectangle newPosition, CollisionDetector collisionDetector) {
        return collisionDetector.isAreaAccessible(newPosition)
            ? newPosition
            : this.baseObject;
    }

    public Direction getDirection() {
        return this.viewDirection;
    }

    public void checkForVerticalViewDirection() {
        if (this.velocity.getVector().y() > 0) {
            viewDirection = Direction.UP;
        } else if (this.velocity.getVector().y() < 0) {
            viewDirection = Direction.DOWN;
        }
    }

    public void checkForHorizontalViewDirection() {
        if (this.velocity.getVector().x() > 0) {
            viewDirection = Direction.RIGHT;
        } else if (this.velocity.getVector().x() < 0) {
            viewDirection = Direction.LEFT;
        }
    }

    protected <S extends Sprite> boolean collidesWith(MovingObject<S> other) {
        return this.baseObject.intersects(other.baseObject);
    }

    public Map.Coordinate getBottomLeft() {
        return this.baseObject.getBottomLeft();
    }

    public Map.Coordinate getTopLeft() {
        return this.baseObject.getTopLeft();
    }
}

