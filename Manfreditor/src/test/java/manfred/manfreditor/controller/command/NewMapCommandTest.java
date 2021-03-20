package manfred.manfreditor.controller.command;

import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.NewMapFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NewMapCommandTest {

    private NewMapCommand.Factory commandFactory;
    private MapModel mapModelMock;
    private NewMapFactory newMapFactoryMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapModelMock = mock(MapModel.class);
        newMapFactoryMock = mock(NewMapFactory.class);
        commandFactory = new NewMapCommand.Factory(mapModelMock, newMapFactoryMock);
    }

    @Test
    void execute() {
        when(newMapFactoryMock.create(any(), any(), any())).thenReturn(mock(Map.class));
        MapModel backupMock = mock(MapModel.class);
        when(mapModelMock.backup()).thenReturn(backupMock);

        CommandResult result = commandFactory.create("name", 1, 2).execute();

        assertThat(result, wasSuccessful());
        verify(mapModelMock).setMap(any());

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(backupMock).restoreStateOf(eq(this.mapModelMock));
    }
}