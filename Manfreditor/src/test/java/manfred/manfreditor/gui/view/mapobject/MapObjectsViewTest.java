package manfred.manfreditor.gui.view.mapobject;

import manfred.manfreditor.mapobject.MapObjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MapObjectsViewTest {

    private MapObjectsView underTest;
    private MapObjectRepository mapObjectRepositoryMock;
    private ObjectsViewCoordinateFactory objectsViewCoordinateFactoryMock;

    @BeforeEach
    void setUp() {
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        objectsViewCoordinateFactoryMock = mock(ObjectsViewCoordinateFactory.class);
        underTest = new MapObjectsView(mapObjectRepositoryMock, objectsViewCoordinateFactoryMock);
    }

    @Test
    void negativeX() {
        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(-1, 0);

        assertTrue(result.isEmpty());
    }

    @Test
    void negativeY() {
        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(0, -1);

        assertTrue(result.isEmpty());
    }

    @Test
    void pointInFirstTile() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(new ObjectsViewCoordinate(0, 0)));
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(mock(MapObjectRepository.ObjectKey.class)));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            MapObjectsView.OBJECT_TILE_SIZE / 2,
            MapObjectsView.OBJECT_TILE_SIZE / 2
        );

        assertTrue(result.isPresent());
    }

    @Test
    void givenOneObject_secondTileSelected_thenResultIsEmpty() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(new ObjectsViewCoordinate(0, 0)));
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(mock(MapObjectRepository.ObjectKey.class)));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            3 * MapObjectsView.OBJECT_TILE_SIZE / 2,
            MapObjectsView.OBJECT_TILE_SIZE / 2
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void givenTwoObjects_firstTileSelected_thenResultIsFirstKey() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0)
        ));
        MapObjectRepository.ObjectKey expectedSelection = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(
            expectedSelection,
            mock(MapObjectRepository.ObjectKey.class)
        ));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            MapObjectsView.OBJECT_TILE_SIZE - 1,
            MapObjectsView.OBJECT_TILE_SIZE - 1
        );

        assertTrue(result.isPresent());
        assertThat(result.get(), is(expectedSelection));
    }

    @Test
    void givenTwoObjects_secondTileSelected_thenResultIsSecondKey() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0)
        ));
        MapObjectRepository.ObjectKey expectedSelection = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(
            mock(MapObjectRepository.ObjectKey.class),
            expectedSelection
        ));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            2 * MapObjectsView.OBJECT_TILE_SIZE - 1,
            MapObjectsView.OBJECT_TILE_SIZE - 1
        );

        assertTrue(result.isPresent());
        assertThat(result.get(), is(expectedSelection));
    }

    @Test
    void givenTwoObjects_thirdTileSelected_thenResultIsEmpty() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0)
        ));
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(
            mock(MapObjectRepository.ObjectKey.class),
            mock(MapObjectRepository.ObjectKey.class)
        ));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            0,
            MapObjectsView.OBJECT_TILE_SIZE
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void givenThreeObjects_thirdTileSelected_thenResultIsThirdKey() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0),
            new ObjectsViewCoordinate(0, 1)
        ));
        MapObjectRepository.ObjectKey expectedSelection = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(
            mock(MapObjectRepository.ObjectKey.class),
            mock(MapObjectRepository.ObjectKey.class),
            expectedSelection
        ));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            MapObjectsView.OBJECT_TILE_SIZE - 1,
            MapObjectsView.OBJECT_TILE_SIZE
        );

        assertTrue(result.isPresent());
        assertThat(result.get(), is(expectedSelection));
    }

    @Test
    void givenThreeObjects_fourthTileSelected_thenResultIsEmpty() {
        when(objectsViewCoordinateFactoryMock.getCoordinates(any())).thenReturn(List.of(
            new ObjectsViewCoordinate(0, 0),
            new ObjectsViewCoordinate(1, 0),
            new ObjectsViewCoordinate(0, 1)
        ));
        when(mapObjectRepositoryMock.getKeys()).thenReturn(List.of(
            mock(MapObjectRepository.ObjectKey.class),
            mock(MapObjectRepository.ObjectKey.class),
            mock(MapObjectRepository.ObjectKey.class)
        ));

        Optional<MapObjectRepository.ObjectKey> result = underTest.getClickedObjectKey(
            MapObjectsView.OBJECT_TILE_SIZE,
            MapObjectsView.OBJECT_TILE_SIZE
        );

        assertTrue(result.isEmpty());
    }
}