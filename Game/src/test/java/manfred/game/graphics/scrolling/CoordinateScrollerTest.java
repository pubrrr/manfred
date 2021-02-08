package manfred.game.graphics.scrolling;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

class CoordinateScrollerTest {

    @Test
    void mapSmallerThanScreen() {
        CoordinateScroller coordinateScroller = CoordinateScroller.buildFrom(10, 100, 5, 1);

        assertThat(coordinateScroller, instanceOf(ConstantScroller.class));
    }

    @Test
    void mapLargerThanScreen() {
        CoordinateScroller coordinateScroller = CoordinateScroller.buildFrom(10,  5, 100, 1);

        assertThat(coordinateScroller, instanceOf(DynamicScroller.class));
    }
}