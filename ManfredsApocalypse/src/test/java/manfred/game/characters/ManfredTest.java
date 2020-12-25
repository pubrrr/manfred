package manfred.game.characters;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class ManfredTest {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private Manfred underTest;

    private MapWrapper mapWrapperMock;

    @BeforeEach
    void init() {
        MapCollider colliderMock = mock(MapCollider.class);
        mapWrapperMock = mock(MapWrapper.class);

        underTest = new Manfred(10, 0, 0, PIXEL_BLOCK_SIZE, PIXEL_BLOCK_SIZE, 1, colliderMock, (new TestGameConfig()).setPixelBlockSize(PIXEL_BLOCK_SIZE), null);
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        Map map = TestMapFactory.create(new String[][]{{"1"}}, null);
        when(mapWrapperMock.getMap()).thenReturn(map);

        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.moveTo();

        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @TestTemplate
    @UseDataProvider("provideInitialManfredCoordinates")
    public void wouldTriggerOnStep(int manfredX, int manfredY, boolean expectTriggerStepOn) {
        underTest.setX(manfredX);
        underTest.setY(manfredY);

        Point resultingMapTile = underTest.moveTo();

        assertEquals(expectTriggerStepOn, resultingMapTile.equals(new Point(0,0)));
    }

    @DataProvider
    static Object[][] provideInitialManfredCoordinates() {
        int halfBlockSize = PIXEL_BLOCK_SIZE / 2;
        return new Object[][]{
            {0, 0, true},
            {halfBlockSize - 1, 0, true},
            {0, halfBlockSize - 1, true},
            {halfBlockSize - 1, halfBlockSize - 1, true},
            {halfBlockSize, 0, false},
            {0, halfBlockSize, false},
            {halfBlockSize, halfBlockSize, false},
        };
    }
}