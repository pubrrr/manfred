package manfred.manfreditor.controller.command;

import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SelectMapObjectCommandTest {

    private SelectMapObjectCommand.Factory commandFactory;
    private MapObjectRepository mapObjectRepositoryMock;
    private SelectedObject selectedObjectMock;

    @BeforeEach
    void setUp() {
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        selectedObjectMock = mock(SelectedObject.class);
        commandFactory = new SelectMapObjectCommand.Factory(mapObjectRepositoryMock, selectedObjectMock);
    }

    @Test
    void selectMapObject() {
        MapObjectRepository.ObjectKey key = mock(MapObjectRepository.ObjectKey.class);
        when(mapObjectRepositoryMock.getKey("key")).thenReturn(Optional.of(key));
        CommandResult result = commandFactory.create("key").execute();

        assertThat(result, wasSuccessful());
        verify(selectedObjectMock).select(eq(key));
    }

    @Test
    void selectFailsBecauseObjectDoesNotExist() {
        when(mapObjectRepositoryMock.getKey("key")).thenReturn(Optional.empty());
        CommandResult result = commandFactory.create("key").execute();

        assertThat(result, failedWithMessage("No map object found in repository with key 'key'"));
    }
}