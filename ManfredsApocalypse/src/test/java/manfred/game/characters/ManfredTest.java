package manfred.game.characters;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;
import manfred.game.map.Accessible;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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

        underTest = new Manfred(10, 0, 0, PIXEL_BLOCK_SIZE, PIXEL_BLOCK_SIZE, 1, colliderMock, mapWrapperMock, (new TestGameConfig()).setPixelBlockSize(PIXEL_BLOCK_SIZE), null);
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        Map map = TestMapFactory.create(new String[][]{{"1"}}, null);
        when(mapWrapperMock.getMap()).thenReturn(map);

        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move();

        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @TestTemplate
    @UseDataProvider("provideInitialManfredCoordinates")
    public void triggerOnStep(int manfredX, int manfredY, boolean expectTriggerStepOn) {
        underTest.setX(manfredX);
        underTest.setY(manfredY);

        Consumer<KeyControls> nonZeroLambda = mock(Consumer.class);
        setup4x4MapMockWithOnStepCallbackTopLeftTile(nonZeroLambda);

        Consumer<KeyControls> result = underTest.move();

        KeyControls keyControlsMock = mock(KeyControls.class);
        result.accept(keyControlsMock);
        verify(keyControlsMock, expectTriggerStepOn ? never() : atLeastOnce()).doNothing();
        verify(nonZeroLambda, expectTriggerStepOn ? atLeastOnce() : never()).accept(keyControlsMock);
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

    private void setup4x4MapMockWithOnStepCallbackTopLeftTile(Consumer<KeyControls> nonZeroLambda) {
        MapTile mapTileMock = mock(MapTile.class);
        when(mapTileMock.onStep()).thenReturn(nonZeroLambda);
        Map map = new Map("test", new MapTile[][]{{mapTileMock, Accessible.getInstance()}, {Accessible.getInstance(), Accessible.getInstance()}}, mock(GameConfig.class));
        when(mapWrapperMock.getMap()).thenReturn(map);
    }

    @Test
    void noGapBetweenTwoNeighboringOnStepTiles() {
        Consumer<KeyControls> nonZeroLambda = keyControls -> {
        };

        MapTile mapTileMock = mock(MapTile.class);
        when(mapTileMock.onStep()).thenReturn(nonZeroLambda);
        Map map = new Map("test", new MapTile[][]{{mapTileMock, mapTileMock}}, mock(GameConfig.class));
        Map mapSpy = spy(map);
        when(mapWrapperMock.getMap()).thenReturn(mapSpy);

        underTest.down();

        while (underTest.getY() <= PIXEL_BLOCK_SIZE) {
            Consumer<KeyControls> result = underTest.move();
            assertNotNull(result);
        }
        verify(mapSpy, atLeastOnce()).stepOn(0, 0);
        verify(mapSpy, atLeastOnce()).stepOn(0, 1);
    }
}