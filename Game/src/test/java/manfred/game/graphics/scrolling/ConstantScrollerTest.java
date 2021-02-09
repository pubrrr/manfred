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
class ConstantScrollerTest {

    @TestTemplate
    @UseDataProvider("provideCoordinates")
    void constantScrollResult(int coordinate) {
        int windowSize = 100;
        int mapSize = 20;
        CoordinateScroller underTest = CoordinateScroller.buildFrom(1, windowSize, mapSize, 0);

        int result = underTest.computeScrollDistance(coordinate);

        assertThat(result, is((windowSize - mapSize) / 2));
    }

    @DataProvider
    static Object[][] provideCoordinates() {
        return new Object[][]{
            {-5},
            {0},
            {5},
            {70},
        };
    }
}