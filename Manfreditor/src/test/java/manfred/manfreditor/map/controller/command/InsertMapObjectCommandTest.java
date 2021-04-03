package manfred.manfreditor.map.controller.command;

import io.vavr.control.Validation;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.view.map.MapView;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import manfred.manfreditor.map.model.mapobject.MapObjectRepository;
import manfred.manfreditor.map.model.mapobject.SelectedObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InsertMapObjectCommandTest {

    private InsertMapObjectCommand.Factory commandFactory;
    private MapView mapViewMock;
    private MapModel mapModelMock;
    private SelectedObject selectedObjectMock;
    private MapObjectRepository mapObjectRepositoryMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapViewMock = mock(MapView.class);
        mapModelMock = mock(MapModel.class);
        selectedObjectMock = mock(SelectedObject.class);
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        commandFactory = new InsertMapObjectCommand.Factory(mapViewMock, mapModelMock, selectedObjectMock, mapObjectRepositoryMock);
    }

    @Test
    void success() {
        Map.TileCoordinate tileToInsertAt = mock(Map.TileCoordinate.class);
        when(selectedObjectMock.getSelection()).thenReturn(Optional.of(mock(MapObjectRepository.ObjectKey.class)));
        when(mapObjectRepositoryMock.get(any())).thenReturn(mock(ConcreteMapObject.class));
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.of(tileToInsertAt));
        when(mapModelMock.tryInsertObjectAt(any(), any())).thenReturn(Validation.valid(null));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(mapModelMock).deleteObjectAt(tileToInsertAt);
    }

    @Test
    void failsWhenNoObjectIsSelected() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.empty());

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessageContaining("Need to select an object before inserting it into the map"));
    }

    @Test
    void failsWhenNoClickedTileWasFound() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.of(mock(MapObjectRepository.ObjectKey.class)));
        when(mapObjectRepositoryMock.get(any())).thenReturn(mock(ConcreteMapObject.class));
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.empty());

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessageContaining("No map tile at clicked coordinates (0,0) was found"));
    }

    @Test
    void failsWhenCannotBeInserted() {
        when(selectedObjectMock.getSelection()).thenReturn(Optional.of(mock(MapObjectRepository.ObjectKey.class)));
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.of(mock(Map.TileCoordinate.class)));
        when(mapObjectRepositoryMock.get(any())).thenReturn(mock(ConcreteMapObject.class));
        when(mapModelMock.tryInsertObjectAt(any(), any())).thenReturn(Validation.invalid(List.of("message1", "message2")));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessageContaining("message1,\nmessage2"));
    }
}