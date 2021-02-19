package manfred.manfreditor.controller;

import manfred.data.InvalidInputException;
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
    private MapProvider mapProviderMock;

    @BeforeEach
    void setUp() {
        mapProviderMock = mock(MapProvider.class);
        commandFactory = new CommandFactory(mapProviderMock);
    }

    @Test
    void execute() throws InvalidInputException {
        Command loadMapCommand = commandFactory.createLoadMapCommand("name");

        CommandResult result = loadMapCommand.execute();

        result.onFailure(s -> {throw new AssertionFailedError("command was not successful: " + s);});
        verify(mapProviderMock).provide("name");
    }

    @Test
    void executeFails() throws InvalidInputException {
        Command loadMapCommand = commandFactory.createLoadMapCommand("name");
        when(mapProviderMock.provide(any())).thenThrow(new InvalidInputException("errorMessage"));

        CommandResult result = loadMapCommand.execute();

        result.onFailure(s -> assertThat(s, is("errorMessage")));
    }
}