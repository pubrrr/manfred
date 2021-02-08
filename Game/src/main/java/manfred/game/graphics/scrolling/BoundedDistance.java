package manfred.game.graphics.scrolling;

import static java.lang.Math.max;
import static java.lang.Math.min;

class BoundedDistance {
    private final int value;

    private BoundedDistance(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public static Factory factory(Interval interval) {
        return new Factory(interval);
    }

    public static class Factory {
        private final Interval interval;

        public Factory(Interval interval) {
            this.interval = interval;
        }

        public BoundedDistance createOfWithinBounds(int value) {
            int validatedValue = max(value, interval.getLowerBound());
            validatedValue = min(validatedValue, interval.getUpperBound());
            return new BoundedDistance(validatedValue);
        }
    }
}
