package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadMapObjectCommandTest {

    private LoadMapObjectCommand.Factory commandFactory;
    private MapTileReader mapTileReaderMock;
    private MapObjectRepository mapObjectRepositoryMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapTileReaderMock = mock(MapTileReader.class);
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        commandFactory = new LoadMapObjectCommand.Factory(mapTileReaderMock, mapObjectRepositoryMock);
    }

    @Test
    void execute() throws InvalidInputException {
        ValidatedMapTileDto validatedMapTileDto = new ValidatedMapTileDto("name", mapMatrixMock(), null, null);
        when(mapTileReaderMock.load(anyString())).thenReturn(validatedMapTileDto);

        Command loadMapTileCommand = commandFactory.create("name");

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, wasSuccessful());
        verify(mapObjectRepositoryMock).populateWith(eq(validatedMapTileDto));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(mapObjectRepositoryMock).delete(any());
    }

    @Test
    void executeFails() throws InvalidInputException {
        Command loadMapTileCommand = commandFactory.create("name");
        when(mapTileReaderMock.load(anyString())).thenThrow(new InvalidInputException("errorMessage"));

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, failedWithMessage("errorMessage"));
    }

    private MapPrototype mapMatrixMock() {
        return mock(MapPrototype.class);
    }
}