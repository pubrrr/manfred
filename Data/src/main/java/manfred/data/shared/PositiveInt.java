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

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public Optional<StrictlyPositiveInt> toStrictlyPositive() {
        return this.value == 0
            ? Optional.empty()
            : Optional.of(StrictlyPositiveInt.of(this.value));
    }
}
