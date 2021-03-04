package manfred.manfreditor.controller.command;

import manfred.manfreditor.gui.view.mapobject.MapObjectsView;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SelectMapObjectCommandTest {

    private SelectMapObjectCommand.Factory commandFactory;
    private MapObjectsView mapObjectsViewMock;
    private SelectedObject selectedObjectMock;

    @BeforeEach
    void setUp() {
        mapObjectsViewMock = mock(MapObjectsView.class);
        selectedObjectMock = mock(SelectedObject.class);
        commandFactory = new SelectMapObjectCommand.Factory(mapObjectsViewMock, selectedObjectMock);
    }

    @Test
    void selectMapObject() {
        MapObjectRepository.ObjectKey key = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectsViewMock.getClickedObjectKey(anyInt(), anyInt())).thenReturn(Optional.of(key));
        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        verify(selectedObjectMock).select(eq(key));
    }

    @Test
    void selectFailsBecauseObjectDoesNotExist() {
        when(mapObjectsViewMock.getClickedObjectKey(anyInt(), anyInt())).thenReturn(Optional.empty());
        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("No map object found at click position (0,0)"));
    }
}