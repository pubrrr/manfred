package manfred.game.characters;

import manfred.game.geometry.Vector;
import manfred.game.map.Map;

public enum Direction {
    RIGHT(Vector.unitVector(100, 0)),
    LEFT(Vector.unitVector(-100, 0)),
    UP(Vector.unitVector(0, 100)),
    DOWN(Vector.unitVector(0, -100));

    private final Vector.Unit<Map.Coordinate> vector;

    Direction(Vector.Unit<Map.Coordinate> vector) {
        this.vector = vector;
    }

    public Vector.Unit<Map.Coordinate> getUnitVector() {
        return this.vector;
    }
}
