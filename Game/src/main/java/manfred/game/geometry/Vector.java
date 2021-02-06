package manfred.game.geometry;

import manfred.data.shared.PositiveInt;

public interface Vector {
    static Vector of(int x, int y) {
        return VectorImpl.of(x, y);
    }

    static Vector pointingUp(int y) {
        return VectorImpl.pointingUp(y);
    }

    static Vector pointingRight(int x) {
        return VectorImpl.pointingRight(x);
    }

    static NonZero nonZero(int x, int y) {
        if (x == 0 && y == 0) {
            throw new IllegalArgumentException("x and y must not both be zero for non zero vector");
        }
        return new NonZero(Vector.of(x, y));
    }

    static Vector zero() {
        return VectorImpl.pointingUp(0);
    }

    int x();

    int y();

    PositiveInt length();

    PositiveInt lengthSquared();

    Vector add(Vector other);

    Vector scale(PositiveInt multiply, PositiveInt.Strict divide);

    int scalarProduct(Vector other);

    boolean pointsTopRight();

    Vector projectOnXAxis();

    Vector projectOnYAxis();

    class NonZero implements Vector {
        private final Vector wrapped;

        private NonZero(Vector wrapped) {
            this.wrapped = wrapped;
        }

        public NonZero scalteToLength(PositiveInt.Strict requiredLength) {
            return new NonZero(wrapped.scale(requiredLength, this.length()));
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
        public Vector add(Vector other) {
            return this.wrapped.add(other);
        }

        @Override
        public Vector scale(PositiveInt multiply, PositiveInt.Strict divide) {
            return this.wrapped.scale(multiply, divide);
        }

        @Override
        public int scalarProduct(Vector other) {
            return this.wrapped.scalarProduct(other);
        }

        @Override
        public boolean pointsTopRight() {
            return this.wrapped.pointsTopRight();
        }

        @Override
        public Vector projectOnXAxis() {
            return this.wrapped.projectOnXAxis();
        }

        @Override
        public Vector projectOnYAxis() {
            return this.wrapped.projectOnYAxis();
        }
    }
}
