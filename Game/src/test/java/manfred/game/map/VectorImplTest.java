package manfred.game.map;

import manfred.game.geometry.Vector;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

class VectorTest {

    @Test
    void length() {
        int someOddLength = 977;

        Vector right = Vector.of(someOddLength, 0);
        assertThat(right.length().value(), is(someOddLength));

        Vector down = Vector.of(0, someOddLength);
        assertThat(down.length().value(), is(someOddLength));

        Vector inclined = Vector.of(someOddLength, 3 * someOddLength);
        double actualLength = someOddLength * Math.sqrt(10);
        assertThat((double) inclined.length().value(), greaterThan(actualLength));
    }
}