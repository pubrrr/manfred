package manfred.game.geometry;

import lombok.EqualsAndHashCode;
import manfred.data.shared.PositiveInt;

@EqualsAndHashCode
class VectorImpl implements Vector {
    private final int x;
    private final int y;

    protected VectorImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    static Vector of(int x, int y) {
        if (x == 0) {
            return VectorImpl.pointingUp(y);
        }
        if (y == 0) {
            return VectorImpl.pointingRight(x);
        }
        return new VectorImpl(x, y);
    }

    static Vector pointingUp(int y) {
        return new VectorImpl.VerticalVector(y);
    }

    static Vector pointingRight(int x) {
        return new VectorImpl.HorizontalVector(x);
    }

    @Override
    public int x() {
        return this.x;
    }

    @Override
    public int y() {
        return this.y;
    }

    @Override
    public PositiveInt length() {
        return PositiveInt.of((int) Math.round(Math.sqrt(x * x + y * y)));
    }

    @Override
    public PositiveInt lengthSquared() {
        return PositiveInt.of(x * x + y * y);
    }

    @Override
    public Vector add(Vector other) {
        return new VectorImpl(this.x + other.x(), this.y + other.y());
    }

    @Override
    public Vector scale(PositiveInt multiply, PositiveInt.Strict divide) {
        return new VectorImpl(this.x * multiply.value() / divide.value(), this.y * multiply.value() / divide.value());
    }

    @Override
    public int scalarProduct(Vector other) {
        return this.x * other.x() + this.y * other.y();
    }

    @Override
    public boolean pointsTopRight() {
        return this.x >= 0 && this.y >= 0;
    }

    @Override
    public Vector projectOnXAxis() {
        return VectorImpl.pointingRight(this.x);
    }

    @Override
    public Vector projectOnYAxis() {
        return VectorImpl.pointingUp(this.y);
    }

    protected static class VerticalVector extends VectorImpl {
        private VerticalVector(int y) {
            super(0, y);
        }

        @Override
        public PositiveInt length() {
            return PositiveInt.of(Math.abs(this.y()));
        }
    }

    protected static class HorizontalVector extends VectorImpl {
        private HorizontalVector(int x) {
            super(x, 0);
        }

        @Override
        public PositiveInt length() {
            return PositiveInt.of(Math.abs(this.x()));
        }
    }
}
