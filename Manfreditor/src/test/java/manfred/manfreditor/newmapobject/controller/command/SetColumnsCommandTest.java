package manfred.manfreditor.newmapobject.controller.command;

import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SetColumnsCommandTest {

    private SetColumnsCommand.Factory commandFactory;
    private NewMapObjectModel newMapObjectModelMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        newMapObjectModelMock = mock(NewMapObjectModel.class);
        commandFactory = new SetColumnsCommand.Factory(newMapObjectModelMock);
    }

    @Test
    void set() {
        when(newMapObjectModelMock.getColumns()).thenReturn(5);

        CommandResult result = commandFactory.create(99).execute();

        assertThat(result, wasSuccessful());
        verify(newMapObjectModelMock).setColumns(99);
        result.registerRollbackOperation(commandHistory);

        commandHistory.undoLast();
        verify(newMapObjectModelMock).setColumns(5);
    }
}