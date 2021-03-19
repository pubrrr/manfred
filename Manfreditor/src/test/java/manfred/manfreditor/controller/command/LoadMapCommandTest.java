package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
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
    private MapProvider mapProviderMock;
    private CommandUrlHelper urlHelperMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapProviderMock = mock(MapProvider.class);
        mapModelMock = mock(MapModel.class);
        urlHelperMock = mock(CommandUrlHelper.class);
        commandFactory = new LoadMapCommand.Factory(mapProviderMock, mapModelMock, urlHelperMock);
    }

    @Test
    void executeSuccessfullyAndRollBack() throws InvalidInputException, MalformedURLException {
        Map resultingMap = mock(Map.class);
        MapModel backupMock = mock(MapModel.class);
        Command loadMapCommand = commandFactory.create("name");
        when(mapProviderMock.provide(any(MapSource.class))).thenReturn(resultingMap);
        when(urlHelperMock.getUrlForFile(any())).thenReturn(new URL("http://some.url"));
        when(mapModelMock.backup()).thenReturn(backupMock);

        CommandResult result = loadMapCommand.execute();

        assertThat(result, wasSuccessful());
        verify(mapModelMock).setMap(resultingMap);

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(backupMock).restoreStateOf(eq(this.mapModelMock));
    }

    @Test
    void executeFails() throws InvalidInputException {
        Command loadMapCommand = commandFactory.create("name");
        when(mapProviderMock.provide(any(MapSource.class))).thenThrow(new InvalidInputException("errorMessage"));

        CommandResult result = loadMapCommand.execute();

        assertThat(result, failedWithMessageContaining("errorMessage"));
    }

    @Test
    void invalidUrl() throws MalformedURLException {
        Command loadMapCommand = commandFactory.create("name");
        when(urlHelperMock.getUrlForFile(any())).thenThrow(new MalformedURLException("message"));

        CommandResult result = loadMapCommand.execute();

        assertThat(result, failedWithMessageContaining("Could not create URL for map file name: message"));
    }
}