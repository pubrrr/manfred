package manfred.data.shared;

import lombok.EqualsAndHashCode;

import java.util.Optional;

@EqualsAndHashCode
public class PositiveInt {
    private final int value;

    private PositiveInt(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static PositiveInt of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be positive, " + value + " given");
        }
        return new PositiveInt(value);
    }

    public static Strict ofNonZero(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be strictly positive, " + value + " given");
        }
        return new Strict(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public Optional<Strict> toStrictlyPositive() {
        return this.value == 0
            ? Optional.empty()
            : Optional.of(ofNonZero(this.value));
    }

    public PositiveInt times(int factor) {
        return new PositiveInt(this.value * factor);
    }

    public PositiveInt divideBy(Strict divisor) {
        return new PositiveInt(this.value / divisor.value());
    }

    public int divideBy(int divisor) {
        return this.value / divisor;
    }

    public PositiveInt add(int number) {
        return new PositiveInt(this.value + number);
    }

    public static class Strict extends PositiveInt{
        protected Strict(int value) {
            super(value);
        }

        @Override
        public Strict times(int factor) {
            return new Strict(this.value() * factor);
        }
    }
}
