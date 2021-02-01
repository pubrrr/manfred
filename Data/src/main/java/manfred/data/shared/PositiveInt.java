package manfred.data.shared;

import lombok.EqualsAndHashCode;
import manfred.data.InvalidInputException;

@EqualsAndHashCode
public class PositiveInt {
    private final int value;

    private PositiveInt(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static PositiveInt of(int value) throws InvalidInputException {
        if (value < 0) {
            throw new InvalidInputException("Value must be positive, " + value + " given");
        }
        return new PositiveInt(value);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
