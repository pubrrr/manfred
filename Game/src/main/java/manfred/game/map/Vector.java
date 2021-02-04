package manfred.game.map;

import lombok.EqualsAndHashCode;
import manfred.data.shared.PositiveInt;
import manfred.data.shared.StrictlyPositiveInt;

@EqualsAndHashCode
public class Vector {
    private final int x;
    private final int y;

    private Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector of(int x, int y) {
        if (x == 0) {
            return Vector.pointingUp(y);
        }
        if (y == 0) {
            return Vector.pointingRight(x);
        }
        return new Vector(x, y);
    }

    public static Vector pointingUp(int y) {
        return new VerticalVector(y);
    }

    public static Vector pointingRight(int x) {
        return new HorizontalVector(x);
    }

    public static Vector zero() {
        return Vector.pointingUp(0);
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public PositiveInt length() {
        return PositiveInt.of((int) Math.round(Math.sqrt(x * x + y * y)));
    }

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector scale(PositiveInt multiply, StrictlyPositiveInt divide) {
        return new Vector(this.x * multiply.value() / divide.value(), this.y * multiply.value() / divide.value());
    }

    public int scalarProduct(Vector vector) {
        return this.x * vector.x + this.y * vector.y;
    }

    private static class VerticalVector extends Vector {
        public VerticalVector(int y) {
            super(0, y);
        }

        @Override
        public PositiveInt length() {
            return PositiveInt.of(Math.abs(this.y()));
        }
    }

    private static class HorizontalVector extends Vector {
        public HorizontalVector(int x) {
            super(x, 0);
        }

        @Override
        public PositiveInt length() {
            return PositiveInt.of(Math.abs(this.x()));
        }
    }
}
