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

    public int times(int factor) {
        return this.value * factor;
    }

    public int divideBy(int divisor) {
        return this.value / divisor;
    }

    public static class Strict extends PositiveInt{
        protected Strict(int value) {
            super(value);
        }
    }
}
