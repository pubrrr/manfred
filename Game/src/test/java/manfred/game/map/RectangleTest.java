package manfred.game.map;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestMapFactory;
import manfred.data.shared.PositiveInt;
import manfred.game.geometry.Rectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class RectangleTest {

    private static final Map coordinateProvider = TestMapFactory.create(new String[][]{
        {"1", "1", "1", "1", "1"},
        {"1", "1", "1", "1", "1"},
        {"1", "1", "1", "1", "1"},
        {"1", "1", "1", "1", "1"},
        {"1", "1", "1", "1", "1"},
    });

    @Test
    void corners() {
        Rectangle<Map.Coordinate> underTest = rectangle(10, 10, 20, 30);

        assertThat(underTest.getBottomLeft(), is(coordinateProvider.coordinateAt(10, 10)));
        assertThat(underTest.getBottomRight(), is(coordinateProvider.coordinateAt(29, 10)));
        assertThat(underTest.getTopLeft(), is(coordinateProvider.coordinateAt(10, 39)));
        assertThat(underTest.getTopRight(), is(coordinateProvider.coordinateAt(29, 39)));
        assertThat(underTest.getCenter(), is(coordinateProvider.coordinateAt(20, 25)));
    }

    @TestTemplate
    @UseDataProvider("provideInsideCoordinates")
    void contains(Map.Coordinate coordinate) {
        Rectangle<Map.Coordinate> underTest = rectangle(10, 10, 20, 30);

        assertTrue(underTest.contains(coordinate));
    }

    @DataProvider
    static Object[][] provideInsideCoordinates() {
        return new Object[][]{
            {coordinateProvider.coordinateAt(10, 10)},
            {coordinateProvider.coordinateAt(29, 10)},
            {coordinateProvider.coordinateAt(10, 39)},
            {coordinateProvider.coordinateAt(29, 39)},
            {coordinateProvider.coordinateAt(15, 15)},
            {coordinateProvider.coordinateAt(29, 15)},
            {coordinateProvider.coordinateAt(20, 39)},
        };
    }

    @TestTemplate
    @UseDataProvider("provideOutsideCoordinates")
    void containsNot(Map.Coordinate coordinate) {
        Rectangle<Map.Coordinate> underTest = rectangle(10, 10, 20, 30);

        assertFalse(underTest.contains(coordinate));
    }

    @DataProvider
    static Object[][] provideOutsideCoordinates() {
        return new Object[][]{
            {coordinateProvider.coordinateAt(0, 0)},
            {coordinateProvider.coordinateAt(9, 9)},
            {coordinateProvider.coordinateAt(10, 9)},
            {coordinateProvider.coordinateAt(9, 10)},
            {coordinateProvider.coordinateAt(30, 40)},
            {coordinateProvider.coordinateAt(30, 15)},
            {coordinateProvider.coordinateAt(20, 40)},
            {coordinateProvider.coordinateAt(100, 100)},
        };
    }

    @TestTemplate
    @UseDataProvider("provideIntersectingRectangles")
    void intersects(Rectangle<Map.Coordinate> other) {
        Rectangle<Map.Coordinate> underTest = rectangle(10, 10, 20, 30);

        assertTrue(underTest.intersects(other));
    }

    @DataProvider
    static Object[][] provideIntersectingRectangles() {
        return new Object[][]{
            {rectangle(5, 5, 6, 6)}, // bottom left corner
            {rectangle(29, 5, 10, 6)}, // bottom right corner
            {rectangle(5, 39, 6, 3)}, // top left corner
            {rectangle(29, 39, 6, 3)}, // top right corner
            {rectangle(5, 5, 100, 10)}, // exceeds horizontally, bottom
            {rectangle(5, 39, 100, 3)}, // exceeds horizontally, top
            {rectangle(5, 5, 10, 100)}, // exceeds vertically, left
            {rectangle(28, 5, 10, 100)}, // exceeds vertically, right
            {rectangle(5, 5, 100, 100)}, // unterTest is contained totally
            {rectangle(20, 20, 2, 2)}, // this is contained totally
            {rectangle(5, 20, 10, 30)}, // form a + together
        };
    }

    @Test
    void intersectionOfExtremelyStretchedRectangles() {
        Rectangle<Map.Coordinate> horizontal = rectangle(0, 5, 100, 5);
        Rectangle<Map.Coordinate> vertical = rectangle(5, 0, 5, 100);

        assertTrue(horizontal.intersects(vertical));
    }

    @TestTemplate
    @UseDataProvider("provideNonIntersectingRectangles")
    void intersectsNot(Rectangle<Map.Coordinate> other) {
        Rectangle<Map.Coordinate> underTest = rectangle(10, 10, 20, 30);

        assertFalse(underTest.intersects(other));
    }

    @DataProvider
    static Object[][] provideNonIntersectingRectangles() {
        return new Object[][]{
            {rectangle(5, 5, 5, 5)}, // bottom left corner
            {rectangle(30, 5, 10, 6)}, // bottom right corner
            {rectangle(5, 40, 6, 3)}, // top left corner
            {rectangle(30, 40, 6, 3)}, // top right corner
            {rectangle(20, 5, 100, 2)},
            {rectangle(5, 20, 2, 100)},
        };
    }

    private static Rectangle<Map.Coordinate> rectangle(int x, int y, int width, int height) {
        return new Rectangle<>(coordinateProvider.coordinateAt(x, y), PositiveInt.of(width), PositiveInt.of(height));
    }
}