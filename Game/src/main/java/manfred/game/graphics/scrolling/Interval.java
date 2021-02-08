package manfred.game.graphics.scrolling;

import lombok.Getter;

import static java.lang.Math.*;

@Getter
public class Interval {
    private final int lowerBound;
    private final int upperBound;

    public Interval(int lowerBound, int upperBound) {
        this.lowerBound = min(lowerBound, upperBound);
        this.upperBound = max(lowerBound, upperBound);
    }
}
