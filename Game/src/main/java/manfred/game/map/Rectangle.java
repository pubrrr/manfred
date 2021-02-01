package manfred.game.map;

import manfred.data.shared.PositiveInt;

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
}
