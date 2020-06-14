package manfred.game.graphics;

import manfred.game.GameConfig;
import manfred.game.characters.Manfred;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BackgroundScrollerTest {
    private BackgroundScroller underTest;

    private Manfred manfredMock;
    private Map mapMock;
    private GameConfig gameConfigMock;

    @BeforeEach
    void init() {
        manfredMock = mock(Manfred.class);

        mapMock = mock(Map.class);
        MapWrapper mapWrapperMock = mock(MapWrapper.class);
        when(mapWrapperMock.getMap()).thenReturn(mapMock);

        gameConfigMock = mock(GameConfig.class);

        underTest = new BackgroundScroller(5, manfredMock, mapWrapperMock, gameConfigMock);
    }

    @Test
    void givenMapSmallerThanScreen_thenCentersMap() {
        assertTrue(false);
    }

}