package manfred.game.graphics.coordinatetransformation;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestMapFactory;
import manfred.data.shared.PositiveInt;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class MapCoordinateToPanelCoordinateTransformerTest {

    private MapCoordinateToPanelCoordinateTransformer underTest;

    private static final Map coordinateProvider = TestMapFactory.create(new String[][]{
        {"1", "1", "1"},
        {"1", "1", "1"}
    });


    @BeforeEach
    void setUp() {
        underTest = new MapCoordinateToPanelCoordinateTransformer(PositiveInt.ofNonZero(10));
    }

    @TestTemplate
    @UseDataProvider("provideCoordinates")
    void toPanelCoordinate(Map.Coordinate coordinate, PanelCoordinate expectedResult) {
        PanelCoordinate actualResult = underTest.toPanelCoordinate(coordinate);

        assertThat(actualResult, is(expectedResult));
    }

    @DataProvider
    static Object[][] provideCoordinates() {
        return new Object[][]{
            {coordinateProvider.coordinateAt(0, 0), new PanelCoordinate(0, 19)}, // bottom left on map
            {coordinateProvider.coordinateAt(0, 119), new PanelCoordinate(0, 0)}, // top left on map
            {coordinateProvider.coordinateAt(179, 0), new PanelCoordinate(29, 19)}, // bottom right on map
            {coordinateProvider.coordinateAt(179, 119), new PanelCoordinate(29, 0)}, // top right on map
            {coordinateProvider.coordinateAt(60, 47), new PanelCoordinate(10, 12)}, // somewhere in the middle
            {coordinateProvider.tileAt(PositiveInt.of(0), PositiveInt.of(0)).getBottomLeftCoordinate(), new PanelCoordinate(0, 19)}, // bottom left map tile
            {coordinateProvider.tileAt(PositiveInt.of(0), PositiveInt.of(1)).getBottomLeftCoordinate(), new PanelCoordinate(0, 9)}, // bottom left map tile
        };
    }
}