package manfred.game.map;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestMapFactory;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class MapColliderTest {
    private static final int PIXEL_BLOCK_SIZE = 60;

    private Map underTest;

    Map initMap(String[][] mapArray) {
        return TestMapFactory.create(mapArray, new HashMap<>());
    }

    @TestTemplate
    @UseDataProvider("provideAccessibleCoords")
    public void collidesNotOnAccessibleField(int x, int y, int size) throws InvalidInputException {
        underTest = initMap(new String[][]{{"1", "1"}, {"1", "1"}});

        Rectangle area = new Rectangle(underTest.coordinateAt(x, y), PositiveInt.of(size), PositiveInt.of(size));

        assertTrue(underTest.isAreaAccessible(area), "accessible");
    }

    @DataProvider
    static Object[][] provideAccessibleCoords() {
        return new Object[][]{
                getBoundaryCoordsByHalfBlockSize(0, 0, PIXEL_BLOCK_SIZE), // top left block only
                getBoundaryCoordsByHalfBlockSize(1, 0, PIXEL_BLOCK_SIZE), // two blocks horizontal
                getBoundaryCoordsByHalfBlockSize(0, 1, PIXEL_BLOCK_SIZE), // two blocks vertical
                getBoundaryCoordsByHalfBlockSize(1, 1, PIXEL_BLOCK_SIZE), // four blocks
        };
    }

    @TestTemplate
    @UseDataProvider("provideNonAccessibleCoords")
    public void collidesOnNotAccessibleField(int x, int y, int size) throws InvalidInputException {
        underTest = initMap(new String[][]{
                {"1", "1", "1"},
                {"1", "0", "1"},
                {"1", "1", "1"},
        });

        Rectangle area = new Rectangle(underTest.coordinateAt(x, y), PositiveInt.of(size), PositiveInt.of(size));

        assertFalse(underTest.isAreaAccessible(area), "accessible");
    }

    @DataProvider
    static Object[][] provideNonAccessibleCoords() {
        return new Object[][]{
                getBoundaryCoordsByHalfBlockSize(2, 2, PIXEL_BLOCK_SIZE), // mid block only
                // circle around the mid 0-block
                getBoundaryCoordsByHalfBlockSize(1, 1, PIXEL_BLOCK_SIZE), // top left
                getBoundaryCoordsByHalfBlockSize(2, 1, PIXEL_BLOCK_SIZE), // top
                getBoundaryCoordsByHalfBlockSize(3, 1, PIXEL_BLOCK_SIZE), // top right
                getBoundaryCoordsByHalfBlockSize(3, 2, PIXEL_BLOCK_SIZE), // right
                getBoundaryCoordsByHalfBlockSize(3, 3, PIXEL_BLOCK_SIZE), // bottom right
                getBoundaryCoordsByHalfBlockSize(2, 3, PIXEL_BLOCK_SIZE), // bottom
                getBoundaryCoordsByHalfBlockSize(1, 3, PIXEL_BLOCK_SIZE), // bottom left
                getBoundaryCoordsByHalfBlockSize(1, 2, PIXEL_BLOCK_SIZE), // left
                // bigger than one block
                getBoundaryCoordsByHalfBlockSize(1, 1, 2 * PIXEL_BLOCK_SIZE),
                getBoundaryCoordsByHalfBlockSize(0, 0, 5 * PIXEL_BLOCK_SIZE / 2),
        };
    }

    private static Object[] getBoundaryCoordsByHalfBlockSize(int halfBlocksX, int halfBlocksY, int size) {
        int x = halfBlocksX * PIXEL_BLOCK_SIZE / 2;
        int y = halfBlocksY * PIXEL_BLOCK_SIZE / 2;
        return new Object[]{x, y, size};
    }
}