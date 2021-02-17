package manfred.game.geometry;

import lombok.EqualsAndHashCode;
import manfred.data.shared.PositiveInt;

@EqualsAndHashCode
class VectorImpl<COORDINATE extends Coordinate<COORDINATE>> implements Vector<COORDINATE> {
    private final int x;
    private final int y;

    protected VectorImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    static <COORDINATE extends Coordinate<COORDINATE>> Vector<COORDINATE> of(int x, int y) {
        if (x == 0) {
            return VectorImpl.pointingUp(y);
        }
        if (y == 0) {
            return VectorImpl.pointingRight(x);
        }
        return new VectorImpl<>(x, y);
    }

    static <COORDINATE extends Coordinate<COORDINATE>> Vector<COORDINATE> pointingUp(int y) {
        return new VectorImpl.VerticalVector<>(y);
    }

    static <COORDINATE extends Coordinate<COORDINATE>> Vector<COORDINATE> pointingRight(int x) {
        return new VectorImpl.HorizontalVector<>(x);
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
    public Vector<COORDINATE> add(Vector<COORDINATE> other) {
        return new VectorImpl<>(this.x + other.x(), this.y + other.y());
    }

    @Override
    public Vector<COORDINATE> scale(PositiveInt multiply, PositiveInt.Strict divide) {
        return new VectorImpl<>(this.x * multiply.value() / divide.value(), this.y * multiply.value() / divide.value());
    }

    @Override
    public int scalarProduct(Vector<COORDINATE> other) {
        return this.x * other.x() + this.y * other.y();
    }

    @Override
    public boolean pointsTopRight() {
        return this.x >= 0 && this.y >= 0;
    }

    @Override
    public Vector<COORDINATE> projectOnXAxis() {
        return VectorImpl.pointingRight(this.x);
    }

    @Override
    public Vector<COORDINATE> projectOnYAxis() {
        return VectorImpl.pointingUp(this.y);
    }

    @Override
    public String toString() {
        return "Vector(" + x + "," + y + ')';
    }

    protected static class VerticalVector<COORDINATE extends Coordinate<COORDINATE>> extends VectorImpl<COORDINATE> {
        private VerticalVector(int y) {
            super(0, y);
        }

        @Override
        public PositiveInt length() {
            return PositiveInt.of(Math.abs(this.y()));
        }
    }

    protected static class HorizontalVector<COORDINATE extends Coordinate<COORDINATE>> extends VectorImpl<COORDINATE> {
        private HorizontalVector(int x) {
            super(x, 0);
        }

        @Override
        public PositiveInt length() {
            return PositiveInt.of(Math.abs(this.x()));
        }
    }
}
