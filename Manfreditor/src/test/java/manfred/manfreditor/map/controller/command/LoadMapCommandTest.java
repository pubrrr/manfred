package manfred.manfreditor.map.controller.command;

import manfred.manfreditor.common.command.Command;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.MapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadMapCommandTest {

    private LoadMapCommand.Factory commandFactory;
    private MapModel mapModelMock;
    private MapRepository mapRepositoryMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapRepositoryMock = mock(MapRepository.class);
        mapModelMock = mock(MapModel.class);
        commandFactory = new LoadMapCommand.Factory(mapRepositoryMock, mapModelMock);
    }

    @Test
    void executeSuccessfullyAndRollBack() {
        Map resultingMap = mock(Map.class);
        MapModel backupMock = mock(MapModel.class);
        MapRepository.MapKey mapKeyMock = mock(MapRepository.MapKey.class);
        Command loadMapCommand = commandFactory.create(mapKeyMock);
        when(mapRepositoryMock.get(any())).thenReturn(resultingMap);
        when(mapModelMock.backup()).thenReturn(backupMock);

        CommandResult result = loadMapCommand.execute();

        assertThat(result, wasSuccessful());
        verify(mapModelMock).setMap(resultingMap);

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(backupMock).restoreStateOf(eq(this.mapModelMock));
    }
}