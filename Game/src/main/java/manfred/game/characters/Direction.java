package manfred.game.characters;

import manfred.game.geometry.Vector;

public enum Direction {
    RIGHT(Vector.nonZero(100, 0)),
    LEFT(Vector.nonZero(-100, 0)),
    UP(Vector.nonZero(0, 100)),
    DOWN(Vector.nonZero(0, -100));

    private final Vector.NonZero vector;

    Direction(Vector.NonZero vector) {
        this.vector = vector;
    }

    public Vector.NonZero getVector() {
        return this.vector;
    }
}
