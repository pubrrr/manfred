package manfred.manfreditor.controller.command;

import manfred.manfreditor.gui.view.map.MapView;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InsertMapObjectCommandTest {

    private InsertMapObjectCommand.Factory commandFactory;
    private MapView mapViewMock;
    private MapModel mapModelMock;
    private SelectedObject selectedObjectMock;
    private MapObjectRepository mapObjectRepositoryMock;

    @BeforeEach
    void setUp() {
        mapViewMock = mock(MapView.class);
        mapModelMock = mock(MapModel.class);
        selectedObjectMock = mock(SelectedObject.class);
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        commandFactory = new InsertMapObjectCommand.Factory(mapViewMock, mapModelMock, selectedObjectMock, mapObjectRepositoryMock);
    }

    @Test
    void success() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.of(mock(MapObjectRepository.ObjectKey.class)));
        when(mapObjectRepositoryMock.get(any())).thenReturn(mock(ConcreteMapObject.class));
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.of(mock(Map.TileCoordinate.class)));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
    }

    @Test
    void failsWhenNoObjectIsSelected() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.empty());

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("Need to select an object before inserting it into the map"));
    }

    @Test
    void failsWhenNoClickedTileWasFound() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.of(mock(MapObjectRepository.ObjectKey.class)));
        when(mapObjectRepositoryMock.get(any())).thenReturn(mock(ConcreteMapObject.class));
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.empty());

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("No map tile at clicked coordinates (0,0) was found"));
    }

    @Test
    void failsWhenCannotBeInserted() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.of(mock(MapObjectRepository.ObjectKey.class)));
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.of(mock(Map.TileCoordinate.class)));
        when(mapObjectRepositoryMock.get(any())).thenReturn(mock(ConcreteMapObject.class));
        when(mapModelMock.insertObjectAt(any(), any())).thenReturn(List.of("message1", "message2"));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("message1, message2"));
    }
}