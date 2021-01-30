package manfred.game.map;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestMapFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class MapCoordinateTest {

    private Map map;

    private Map.Coordinate origin;

    @BeforeEach
    void setUp() {
        map = TestMapFactory.create(new String[][]{
            {"1", "1", "1"},
            {"1", "1", "1"}
        });
        origin = map.coordinateAt(0, 0);
    }

    @TestTemplate
    @UseDataProvider("provideCoordinateAndExpectedCoordinate")
    void bounds(Point input, Point expected) {
        Map.Coordinate coordinate = map.coordinateAt(input.x, input.y);

        Vector toCoordinate = origin.distanceTo(coordinate);
        assertThat(toCoordinate.x(), is(expected.x));
        assertThat(toCoordinate.y(), is(expected.y));
    }

    @DataProvider
    static Object[][] provideCoordinateAndExpectedCoordinate() {
        return new Object[][]{
            {new Point(0, 0), new Point(0, 0)},
            {new Point(0, 199), new Point(0, 199)},
            {new Point(299, 0), new Point(299, 0)},
            {new Point(299, 199), new Point(299, 199)},
            {new Point(-5, -5), new Point(0, 0)},
            {new Point(-5, 5), new Point(0, 5)},
            {new Point(5, -5), new Point(5, 0)},
            {new Point(999, 999), new Point(299, 199)},
        };
    }

    @TestTemplate
    @UseDataProvider("provideTranslations")
    void distanceToAndTranslate(Point input) {
        Map.Coordinate coordinate = map.coordinateAt(input.x, input.y);

        Map.Coordinate result = origin.translate(origin.distanceTo(coordinate));
        assertThat(result, equalTo(coordinate));
    }

    @DataProvider
    static Object[][] provideTranslations() {
        return new Object[][]{
            {new Point(0, 0)},
            {new Point(0, 199)},
            {new Point(299, 0)},
            {new Point(299, 199)},
            {new Point(-5, -5)},
            {new Point(-5, 5)},
            {new Point(5, -5)},
            {new Point(999, 999)},
            {new Point(100, 100)},
        };
    }
}