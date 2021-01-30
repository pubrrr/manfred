package manfred.game.map;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

class VectorTest {

    @Test
    void length() {
        int someOddLength = 977;

        Vector right = Vector.of(someOddLength, 0);
        assertThat(right.length(), is(someOddLength));

        Vector down = Vector.of(0, someOddLength);
        assertThat(down.length(), is(someOddLength));

        Vector inclined = Vector.of(someOddLength, 3 * someOddLength);
        double actualLength = someOddLength * Math.sqrt(10);
        assertThat((double) inclined.length(), greaterThan(actualLength));
    }
}