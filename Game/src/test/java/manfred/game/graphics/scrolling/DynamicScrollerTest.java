package manfred.game.graphics.scrolling;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class DynamicScrollerTest {
    private static final int WINDOW_SIZE = 30;
    private static final int MAP_SIZE = 200;
    private static final int TRIGGER_DISTANCE = 10;

    @TestTemplate
    @UseDataProvider("provideCoordinatesWithExpectationForStartingLeft")
    void startingLeft(int coordinate, int expectedResult) {
        CoordinateScroller underTest = CoordinateScroller.buildFrom(TRIGGER_DISTANCE, WINDOW_SIZE, MAP_SIZE, 0);

        int actual = underTest.computeScrollDistance(coordinate);

        assertThat(actual, is(expectedResult));
    }

    @DataProvider
    static Object[][] provideCoordinatesWithExpectationForStartingLeft() {
        return new Object[][]{
            {0, 0},
            {TRIGGER_DISTANCE, 0},
            {TRIGGER_DISTANCE + 1, 0},
            {WINDOW_SIZE - TRIGGER_DISTANCE, 0},
            {WINDOW_SIZE - TRIGGER_DISTANCE + 1, -1},
            {WINDOW_SIZE, -10},
            {MAP_SIZE / 2, - (MAP_SIZE / 2 - WINDOW_SIZE + TRIGGER_DISTANCE)},
        };
    }

    @TestTemplate
    @UseDataProvider("provideCoordinatesWithExpectationForStartingMiddle")
    void startingInTheMiddle(int coordinate, int expectedResult) {
        CoordinateScroller underTest = CoordinateScroller.buildFrom(TRIGGER_DISTANCE, WINDOW_SIZE, MAP_SIZE, MAP_SIZE / 2);

        int actual = underTest.computeScrollDistance(coordinate);

        assertThat(actual, is(expectedResult));
    }

    @DataProvider
    static Object[][] provideCoordinatesWithExpectationForStartingMiddle() {
        return new Object[][]{
            {0,                                                         0},
            {TRIGGER_DISTANCE,                                          0},
            {MAP_SIZE / 2  - WINDOW_SIZE / 2 + TRIGGER_DISTANCE - 1,    - (MAP_SIZE - WINDOW_SIZE) / 2 + 1},
            {MAP_SIZE / 2  - WINDOW_SIZE / 2 + TRIGGER_DISTANCE,        - (MAP_SIZE - WINDOW_SIZE) / 2},
            {MAP_SIZE / 2,                                              - (MAP_SIZE - WINDOW_SIZE) / 2},
            {MAP_SIZE / 2  + WINDOW_SIZE / 2 - TRIGGER_DISTANCE,        - (MAP_SIZE - WINDOW_SIZE) / 2},
            {MAP_SIZE / 2  + WINDOW_SIZE / 2 - TRIGGER_DISTANCE + 1,    - (MAP_SIZE - WINDOW_SIZE) / 2 - 1},
            {MAP_SIZE - TRIGGER_DISTANCE,                               WINDOW_SIZE - MAP_SIZE},
        };
    }

    @TestTemplate
    @UseDataProvider("provideCoordinatesWithExpectationForStartingRight")
    void startingRight(int coordinate, int expectedResult) {
        CoordinateScroller underTest = CoordinateScroller.buildFrom(TRIGGER_DISTANCE, WINDOW_SIZE, MAP_SIZE, MAP_SIZE);

        int actual = underTest.computeScrollDistance(coordinate);

        assertThat(actual, is(expectedResult));
    }

    @DataProvider
    static Object[][] provideCoordinatesWithExpectationForStartingRight() {
        return new Object[][]{
            {0,                                                 0},
            {MAP_SIZE  - WINDOW_SIZE,                           WINDOW_SIZE - MAP_SIZE + TRIGGER_DISTANCE},
            {MAP_SIZE  - WINDOW_SIZE + TRIGGER_DISTANCE - 1,    WINDOW_SIZE - MAP_SIZE + 1},
            {MAP_SIZE  - WINDOW_SIZE + TRIGGER_DISTANCE,        WINDOW_SIZE - MAP_SIZE},
            {MAP_SIZE - TRIGGER_DISTANCE,                       WINDOW_SIZE - MAP_SIZE},
            {MAP_SIZE,                                          WINDOW_SIZE - MAP_SIZE},
        };
    }
}