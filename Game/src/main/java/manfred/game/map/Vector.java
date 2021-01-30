package manfred.game.map;

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

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int length() {
        return (int) Math.round(Math.sqrt(x * x + y * y));
    }

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector scale(int multiply, int divide) {
        return new Vector(this.x * multiply / divide, this.y * multiply / divide);
    }

    private Vector scale(int multiply) {
        return new Vector(this.x * multiply, this.y * multiply);
    }

    private static class VerticalVector extends Vector {
        public VerticalVector(int y) {
            super(0, y);
        }

        @Override
        public int length() {
            return this.y();
        }
    }

    private static class HorizontalVector extends Vector {
        public HorizontalVector(int x) {
            super(x, 0);
        }

        @Override
        public int length() {
            return this.x();
        }
    }
}
