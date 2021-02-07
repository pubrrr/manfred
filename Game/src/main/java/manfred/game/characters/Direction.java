package manfred.game.characters;

import manfred.game.geometry.Vector;
import manfred.game.map.Map;

public enum Direction {
    RIGHT(Vector.nonZero(100, 0)),
    LEFT(Vector.nonZero(-100, 0)),
    UP(Vector.nonZero(0, 100)),
    DOWN(Vector.nonZero(0, -100));

    private final Vector.NonZero<Map.Coordinate> vector;

    Direction(Vector.NonZero<Map.Coordinate> vector) {
        this.vector = vector;
    }

    public Vector.NonZero<Map.Coordinate> getVector() {
        return this.vector;
    }
}
