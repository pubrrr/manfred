package manfred.manfreditor.controller.command;

import manfred.manfreditor.gui.view.mapobject.MapObjectsView;
import manfred.manfreditor.mapobject.KeySelection;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import manfred.manfreditor.mapobject.SelectionState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SelectMapObjectCommandTest {

    private SelectMapObjectCommand.Factory commandFactory;
    private MapObjectsView mapObjectsViewMock;
    private SelectedObject selectedObject;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapObjectsViewMock = mock(MapObjectsView.class);
        selectedObject = mock(SelectedObject.class);
        commandFactory = new SelectMapObjectCommand.Factory(this.mapObjectsViewMock, this.selectedObject);
    }

    @Test
    void selectMapObject() {
        MapObjectRepository.ObjectKey key = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectsViewMock.getClickedObjectKey(anyInt(), anyInt())).thenReturn(Optional.of(key));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        verify(selectedObject).select(eq(key));
    }

    @Test
    void selectFailsBecauseObjectDoesNotExist() {
        when(mapObjectsViewMock.getClickedObjectKey(anyInt(), anyInt())).thenReturn(Optional.empty());

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("No map object found at click position (0,0)"));
    }

    @Test
    void rollbackWhenNothingWasSelectedPreviously() {
        setupFactoryWithRealSelectedObject(SelectionState.empty());
        MapObjectRepository.ObjectKey key = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectsViewMock.getClickedObjectKey(anyInt(), anyInt())).thenReturn(Optional.of(key));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        assertThat(selectedObject.getSelection(), is(Optional.of(key)));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();

        assertThat(selectedObject.getSelection(), is(Optional.empty()));
    }

    @Test
    void rollbackWithAnotherPreviouslySelectedObject() {
        var initiallySelectedKey = mock(MapObjectRepository.ObjectKey.class);
        setupFactoryWithRealSelectedObject(new KeySelection(initiallySelectedKey));
        var key = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectsViewMock.getClickedObjectKey(anyInt(), anyInt())).thenReturn(Optional.of(key));

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        assertThat(selectedObject.getSelection(), is(Optional.of(key)));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();

        assertThat(selectedObject.getSelection(), is(Optional.of(initiallySelectedKey)));
    }

    private void setupFactoryWithRealSelectedObject(SelectionState initialSelection) {
        this.selectedObject = new SelectedObject(initialSelection);
        this.commandFactory = new SelectMapObjectCommand.Factory(this.mapObjectsViewMock, this.selectedObject);
    }
}