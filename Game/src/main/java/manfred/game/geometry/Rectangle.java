package manfred.game.geometry;

import lombok.ToString;
import manfred.data.shared.PositiveInt;

@ToString
public class Rectangle<COORDINATE extends Coordinate<COORDINATE>> {
    private final COORDINATE bottomLeft;
    private final PositiveInt width;
    private final PositiveInt height;

    public Rectangle(COORDINATE bottomLeft, PositiveInt width, PositiveInt height) {
        this.bottomLeft = bottomLeft;
        this.width = width;
        this.height = height;
    }

    public COORDINATE getBottomLeft() {
        return this.bottomLeft;
    }

    public COORDINATE getTopRight() {
        return this.bottomLeft
            .translate(Vector.pointingRight(width.value() - 1))
            .translate(Vector.pointingUp(height.value() - 1));
    }

    public COORDINATE getTopLeft() {
        return this.bottomLeft.translate(Vector.pointingUp(height.value() - 1));
    }

    public COORDINATE getBottomRight() {
        return this.bottomLeft.translate(Vector.pointingRight(width.value() - 1));
    }

    public Rectangle<COORDINATE> translate(Vector<COORDINATE> vector) {
        return new Rectangle<>(bottomLeft.translate(vector), this.width, this.height);
    }

    public Rectangle<COORDINATE> moveTo(COORDINATE newBottomLeft) {
        return new Rectangle<>(newBottomLeft, this.width, this.height);
    }

    public COORDINATE getCenter() {
        return this.bottomLeft
            .translate(Vector.pointingRight(width.value() / 2))
            .translate(Vector.pointingUp(height.value() / 2));
    }

    public boolean intersects(Rectangle<COORDINATE> other) {
        Vector<COORDINATE> thisBottomLeftToOtherRopRight = this.bottomLeft.distanceTo(other.getTopRight());
        Vector<COORDINATE> otherBottomLeftToThisTopRight = other.bottomLeft.distanceTo(this.getTopRight());

        return thisBottomLeftToOtherRopRight.pointsTopRight()
            && otherBottomLeftToThisTopRight.pointsTopRight();
    }

    public boolean contains(COORDINATE coordinate) {
        Vector<COORDINATE> bottomLeftToCoordinate = this.bottomLeft.distanceTo(coordinate);
        Vector<COORDINATE> coordinateToTopRight = coordinate.distanceTo(this.getTopRight());

        return bottomLeftToCoordinate.pointsTopRight() && coordinateToTopRight.pointsTopRight();
    }
}
