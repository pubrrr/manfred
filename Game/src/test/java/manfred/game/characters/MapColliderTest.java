package manfred.game.characters;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class MapColliderTest {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private MapCollider underTest;

    void initMap(String[][] mapArray) {
        Map map = TestMapFactory.create(mapArray, new HashMap<>());

        MapWrapper mapWrapperMock = mock(MapWrapper.class);
        when(mapWrapperMock.getMap()).thenReturn(map);

        underTest = new MapCollider(mapWrapperMock, (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @TestTemplate
    @UseDataProvider("provideAccessibleCoords")
    public void collidesNotOnAccessibleField(int left, int right, int top, int bottom) {
        initMap(new String[][]{{"1", "1"}, {"1", "1"}});

        assertFalse(underTest.collides(left, right, top, bottom), "accessible");
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
    public void collidesOnNotAccessibleField(int left, int right, int top, int bottom) {
        initMap(new String[][]{
                {"1", "1", "1"},
                {"1", "0", "1"},
                {"1", "1", "1"},
        });

        assertTrue(underTest.collides(left, right, top, bottom), "accessible");
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

    @TestTemplate
    @UseDataProvider("provideOutOfBounds")
    public void outOfBounds(int left, int right, int top, int bottom) {
        initMap(new String[][]{{"0"}});

        assertTrue(underTest.collides(left, right, top, bottom), "accessible");
    }

    @DataProvider
    static Object[][] provideOutOfBounds() {
        return new Object[][]{
                {-1, 0, 0, 0,}, // out of bound left
                {0, 5 * PIXEL_BLOCK_SIZE, 0, 0,}, // out of bound right
                {0, 0, -1, 0,}, // out of bound up
                {0, 0, 0, 5 * PIXEL_BLOCK_SIZE,}, // out of bound down
        };
    }

    private static Object[] getBoundaryCoordsByHalfBlockSize(int halfBlocksX, int halfBlocksY, int size) {
        int x = halfBlocksX * PIXEL_BLOCK_SIZE / 2;
        int y = halfBlocksY * PIXEL_BLOCK_SIZE / 2;
        return new Object[]{
                x,
                x + size - 1,
                y,
                y + size - 1
        };
    }
}