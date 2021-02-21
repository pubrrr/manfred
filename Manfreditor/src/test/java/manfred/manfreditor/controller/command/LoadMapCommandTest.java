package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadMapCommandTest extends CommandTestCase {

    private LoadMapCommand.Factory commandFactory;
    private MapModel mapModelMock;
    private MapProvider mapProviderMock;

    @BeforeEach
    void setUp() {
        mapProviderMock = mock(MapProvider.class);
        mapModelMock = mock(MapModel.class);
        commandFactory = new LoadMapCommand.Factory(mapProviderMock, mapModelMock);
    }

    @Test
    void execute() throws InvalidInputException {
        Map resultingMap = mock(Map.class);
        Command loadMapCommand = commandFactory.create("name");
        when(mapProviderMock.provide("name")).thenReturn(resultingMap);

        CommandResult result = loadMapCommand.execute();

        result.onFailure(s -> { throw new AssertionFailedError("command was not successful: " + s); });
        verify(mapModelMock).setMap(resultingMap);
    }

    @Test
    void executeFails() throws InvalidInputException {
        Command loadMapCommand = commandFactory.create("name");
        when(mapProviderMock.provide(any())).thenThrow(new InvalidInputException("errorMessage"));

        CommandResult result = loadMapCommand.execute();

        assertCommandFailed(result, "errorMessage");
    }
}