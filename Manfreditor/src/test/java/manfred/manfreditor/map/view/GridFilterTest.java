package manfred.manfreditor.map.view;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.view.mapobject.ObjectsGridCoordinate;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class GridFilterTest {

    public static final int TILE_PIXEL_SIZE = 10;

    @TestTemplate
    @UseDataProvider("provideIdCoordinates")
    void testApplies(int clickedX, int clickedY) {
        Map<ObjectsGridCoordinate, String> testGrid = Map.of(
            gridCoordinate(0, 0), "",
            gridCoordinate(1, 0), "",
            gridCoordinate(2, 0), "",
            gridCoordinate(0, 1), "",
            gridCoordinate(1, 1), "",
            gridCoordinate(2, 1), "",
            gridCoordinate(0, 2), "",
            gridCoordinate(1, 2), "",
            gridCoordinate(2, 2), ""
        );

        Optional<ObjectsGridCoordinate> result = testGrid.entrySet()
            .stream()
            .filter(GridFilter.tileWithSizeContains(TILE_PIXEL_SIZE, clickedX, clickedY))
            .map(Map.Entry::getKey)
            .findAny();

        assertThat(result, is(Optional.of(gridCoordinate(1, 1))));
    }

    private ObjectsGridCoordinate gridCoordinate(int x, int y) {
        return new ObjectsGridCoordinate(PositiveInt.of(x), PositiveInt.of(y));
    }

    @DataProvider
    static Object[][] provideIdCoordinates() {
        return new Object[][]{
            new Object[]{10, 10},
            new Object[]{10, 19},
            new Object[]{19, 10},
            new Object[]{19, 19},
            new Object[]{15, 15},
        };
    }
}