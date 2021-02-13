package manfred.game.graphics;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import manfred.game.geometry.Vector;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
public class PanelCoordinateTest {

    @TestTemplate
    @UseDataProvider("provideCoordinatesToCompareWithExpectedResult")
    void compare(PanelCoordinate first, PanelCoordinate second, int expectedResult) {
        int actualResult = first.compareTo(second);

        assertThat(actualResult, is(expectedResult));
    }

    @DataProvider
    static Object[][] provideCoordinatesToCompareWithExpectedResult() {
        return new Object[][]{
            {coordinate(0, 0), coordinate(0, 0), 0},

            {coordinate(1, 0), coordinate(0, 0), 1},
            {coordinate(0, 0), coordinate(1, 0), -1},

            {coordinate(0, 1), coordinate(0, 0), 1},
            {coordinate(0, 0), coordinate(0, 1), -1},

            {coordinate(0, 1), coordinate(1, 0), 1},
            {coordinate(1, 0), coordinate(0, 1), -1},

            {coordinate(1, 1), coordinate(0, 0), 1},
            {coordinate(0, 0), coordinate(1, 1), -1},
        };
    }

    @TestTemplate
    @UseDataProvider("provideTranslationsAndExpectedResult")
    void translate(Vector<PanelCoordinate> second, PanelCoordinate expectedResult) {
        PanelCoordinate actualResult = coordinate(0, 0).translate(second);

        assertThat(actualResult, is(expectedResult));
    }

    @DataProvider
    static Object[][] provideTranslationsAndExpectedResult() {
        return new Object[][]{
            {translation(0, 0), coordinate(0, 0)},
            {translation(1, 0), coordinate(1, 0)},
            {translation(1, 1), coordinate(1, -1)},
            {translation(0, 1), coordinate(0, -1)},
            {translation(-1, 1), coordinate(-1, -1)},
            {translation(-1, 0), coordinate(-1, 0)},
            {translation(-1, -1), coordinate(-1, 1)},
            {translation(0, -1), coordinate(0, 1)},
            {translation(1, -1), coordinate(1, 1)},
        };
    }

    private static PanelCoordinate coordinate(int x, int y) {
        return new PanelCoordinate(x, y);
    }

    private static Vector<PanelCoordinate> translation(int x, int y) {
        return Vector.of(x, y);
    }
}
