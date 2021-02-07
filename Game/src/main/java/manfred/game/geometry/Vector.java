package manfred.game.geometry;

import manfred.data.shared.PositiveInt;

public interface Vector<COORDINATE> {
    static <COORDINATE> Vector<COORDINATE> of(int x, int y) {
        return VectorImpl.of(x, y);
    }

    static <COORDINATE> Vector<COORDINATE> pointingUp(int y) {
        return VectorImpl.pointingUp(y);
    }

    static <COORDINATE> Vector<COORDINATE> pointingRight(int x) {
        return VectorImpl.pointingRight(x);
    }

    static <COORDINATE> NonZero<COORDINATE> nonZero(int x, int y) {
        if (x == 0 && y == 0) {
            throw new IllegalArgumentException("x and y must not both be zero for non zero vector");
        }
        return new NonZero<>(Vector.of(x, y));
    }

    static <COORDINATE> Vector<COORDINATE> zero() {
        return VectorImpl.pointingUp(0);
    }

    int x();

    int y();

    PositiveInt length();

    PositiveInt lengthSquared();

    Vector<COORDINATE> add(Vector<COORDINATE> other);

    Vector<COORDINATE> scale(PositiveInt multiply, PositiveInt.Strict divide);

    int scalarProduct(Vector<COORDINATE> other);

    boolean pointsTopRight();

    Vector<COORDINATE> projectOnXAxis();

    Vector<COORDINATE> projectOnYAxis();

    class NonZero<COORDINATE> implements Vector<COORDINATE> {
        private final Vector<COORDINATE> wrapped;

        private NonZero(Vector<COORDINATE> wrapped) {
            this.wrapped = wrapped;
        }

        public NonZero<COORDINATE> scalteToLength(PositiveInt.Strict requiredLength) {
            return new NonZero<>(wrapped.scale(requiredLength, this.length()));
        }

        @Override
        public int x() {
            return wrapped.x();
        }

        @Override
        public int y() {
            return wrapped.y();
        }

        @Override
        public PositiveInt.Strict length() {
            return PositiveInt.ofNonZero(this.wrapped.length().value());
        }

        @Override
        public PositiveInt.Strict lengthSquared() {
            return PositiveInt.ofNonZero(this.wrapped.lengthSquared().value());
        }

        @Override
        public Vector<COORDINATE> add(Vector<COORDINATE> other) {
            return this.wrapped.add(other);
        }

        @Override
        public Vector<COORDINATE> scale(PositiveInt multiply, PositiveInt.Strict divide) {
            return this.wrapped.scale(multiply, divide);
        }

        @Override
        public int scalarProduct(Vector<COORDINATE> other) {
            return this.wrapped.scalarProduct(other);
        }

        @Override
        public boolean pointsTopRight() {
            return this.wrapped.pointsTopRight();
        }

        @Override
        public Vector<COORDINATE> projectOnXAxis() {
            return this.wrapped.projectOnXAxis();
        }

        @Override
        public Vector<COORDINATE> projectOnYAxis() {
            return this.wrapped.projectOnYAxis();
        }
    }
}
