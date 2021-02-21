package manfred.manfreditor.controller;

import manfred.data.InvalidInputException;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.MapProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadMapCommandTest {

    private CommandFactory commandFactory;
    private MapModel mapModelMock;
    private MapProvider mapProviderMock;

    @BeforeEach
    void setUp() {
        mapProviderMock = mock(MapProvider.class);
        mapModelMock = mock(MapModel.class);
        commandFactory = new CommandFactory(mapProviderMock, mapModelMock);
    }

    @Test
    void execute() throws InvalidInputException {
        Map resultingMap = mock(Map.class);
        Command loadMapCommand = commandFactory.createLoadMapCommand("name");
        when(mapProviderMock.provide("name")).thenReturn(resultingMap);

        CommandResult result = loadMapCommand.execute();

        result.onFailure(s -> {throw new AssertionFailedError("command was not successful: " + s);});
        verify(mapModelMock).setMap(resultingMap);
    }

    @Test
    void executeFails() throws InvalidInputException {
        Command loadMapCommand = commandFactory.createLoadMapCommand("name");
        when(mapProviderMock.provide(any())).thenThrow(new InvalidInputException("errorMessage"));

        CommandResult result = loadMapCommand.execute();

        result.onFailure(errorMessage -> assertThat(errorMessage, is("errorMessage")));
    }
}