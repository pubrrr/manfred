package manfred.data.shared;

public class StrictlyPositiveInt {
    private final int value;

    private StrictlyPositiveInt(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public static StrictlyPositiveInt of(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value must be strictly positive, " + value + " given");
        }
        return new StrictlyPositiveInt(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
