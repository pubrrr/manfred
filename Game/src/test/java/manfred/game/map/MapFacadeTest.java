package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Manfred;
import manfred.infrastructureadapter.map.MapProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static helpers.TestMapFactory.tileAt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MapFacadeTest {

    private MapFacade underTest;
    private MapProvider mapProviderMock;

    @BeforeEach
    void setUp() {
        mapProviderMock = mock(MapProvider.class);
        underTest = new MapFacade(mapProviderMock, mock(Map.class));
    }

    @Test
    void loadMap() throws InvalidInputException {
        Manfred manfredMock = mock(Manfred.class);
        PositiveInt spawnX = PositiveInt.of(2);
        PositiveInt spawnY = PositiveInt.of(3);

        Map resultingMapMock = mock(Map.class);
        when(resultingMapMock.tileAt(eq(spawnX), eq(spawnY))).thenReturn(tileAt(spawnX, spawnY));
        when(mapProviderMock.provide(anyString())).thenReturn(resultingMapMock);

        underTest.loadMap("testName", new ManfredPositionSetter(manfredMock, spawnX, spawnY));

        verify(manfredMock).setToTile(tileAt(spawnX, spawnY));
    }
}