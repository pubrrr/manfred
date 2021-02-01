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
    private static final int TILE_SIZE = 60;


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
            {new Point(0, 119), new Point(0, 119)},
            {new Point(179, 0), new Point(179, 0)},
            {new Point(179, 119), new Point(179, 119)},
            {new Point(-5, -5), new Point(0, 0)},
            {new Point(-5, 5), new Point(0, 5)},
            {new Point(5, -5), new Point(5, 0)},
            {new Point(999, 999), new Point(179, 119)},
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
            {new Point(0, 119)},
            {new Point(179, 0)},
            {new Point(179, 119)},
            {new Point(-5, -5)},
            {new Point(-5, 5)},
            {new Point(5, -5)},
            {new Point(999, 999)},
            {new Point(100, 100)},
        };
    }

    @TestTemplate
    @UseDataProvider("provideCoordinates")
    void coordinateToTile(Point input, Point expected) {
        Map.TileCoordinate actual = map.coordinateAt(input.x, input.y).getTile();

        Map.Coordinate expectedCoordinate = map.coordinateAt(expected.x * TILE_SIZE, expected.y * TILE_SIZE);
        assertThat(actual.getBottomLeftCoordinate(), equalTo(expectedCoordinate));
    }

    @DataProvider
    static Object[][] provideCoordinates() {
        return new Object[][]{
            {new Point(0, 0), new Point(0, 0)},
            {new Point(TILE_SIZE - 1, TILE_SIZE - 1), new Point(0, 0)},
            {new Point(TILE_SIZE - 1, 0), new Point(0, 0)},
            {new Point(0, TILE_SIZE - 1), new Point(0, 0)},
            {new Point(TILE_SIZE, 0), new Point(1, 0)},
            {new Point(0, TILE_SIZE), new Point(0, 1)},
            {new Point(TILE_SIZE, TILE_SIZE), new Point(1, 1)},
        };
    }
}