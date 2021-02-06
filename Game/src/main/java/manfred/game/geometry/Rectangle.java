package manfred.game.geometry;

import lombok.ToString;
import manfred.data.shared.PositiveInt;
import manfred.game.map.Map;

@ToString
public class Rectangle {
    private final Map.Coordinate bottomLeft;
    private final PositiveInt width;
    private final PositiveInt height;

    public Rectangle(Map.Coordinate bottomLeft, PositiveInt width, PositiveInt height) {
        this.bottomLeft = bottomLeft;
        this.width = width;
        this.height = height;
    }

    public Map.Coordinate getBottomLeft() {
        return this.bottomLeft;
    }

    public Map.Coordinate getTopRight() {
        return this.bottomLeft
            .translate(Vector.pointingRight(width.value() - 1))
            .translate(Vector.pointingUp(height.value() - 1));
    }

    public Map.Coordinate getTopLeft() {
        return this.bottomLeft.translate(Vector.pointingUp(height.value() - 1));
    }

    public Map.Coordinate getBottomRight() {
        return this.bottomLeft.translate(Vector.pointingRight(width.value() - 1));
    }

    public Rectangle translate(Vector vector) {
        return new Rectangle(bottomLeft.translate(vector), this.width, this.height);
    }

    public Rectangle moveTo(Map.Coordinate newBottomLeft) {
        return new Rectangle(newBottomLeft, this.width, this.height);
    }

    public Map.Coordinate getCenter() {
        return this.bottomLeft
            .translate(Vector.pointingRight(width.value() / 2))
            .translate(Vector.pointingUp(height.value() / 2));
    }

    public boolean intersects(Rectangle other) {
        Vector thisBottomLeftToOtherRopRight = this.bottomLeft.distanceTo(other.getTopRight());
        Vector otherBottomLeftToThisTopRight = other.bottomLeft.distanceTo(this.getTopRight());

        return thisBottomLeftToOtherRopRight.pointsTopRight()
            && otherBottomLeftToThisTopRight.pointsTopRight();
    }

    public boolean contains(Map.Coordinate coordinate) {
        Vector bottomLeftToCoordinate = this.bottomLeft.distanceTo(coordinate);
        Vector coordinateToTopRight = coordinate.distanceTo(this.getTopRight());

        return bottomLeftToCoordinate.pointsTopRight() && coordinateToTopRight.pointsTopRight();
    }
}
