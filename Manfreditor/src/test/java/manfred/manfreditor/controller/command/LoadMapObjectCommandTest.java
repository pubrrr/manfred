package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadMapObjectCommandTest {

    private LoadMapObjectCommand.Factory commandFactory;
    private MapTileReader mapTileReaderMock;
    private MapObjectRepository mapObjectRepositoryMock;

    @BeforeEach
    void setUp() {
        mapTileReaderMock = mock(MapTileReader.class);
        mapObjectRepositoryMock = mock(MapObjectRepository.class);
        commandFactory = new LoadMapObjectCommand.Factory(mapTileReaderMock, mapObjectRepositoryMock);
    }

    @Test
    void execute() throws InvalidInputException {
        ValidatedMapTileDto validatedMapTileDto = new ValidatedMapTileDto("name", mapMatrixMock(), null);
        when(mapTileReaderMock.load(any())).thenReturn(validatedMapTileDto);

        Command loadMapTileCommand = commandFactory.create("name");

        CommandResult result = loadMapTileCommand.execute();

        result.onFailure(s -> {throw new AssertionFailedError("command was not successful: " + s);});
        verify(mapObjectRepositoryMock).populateWith(eq(validatedMapTileDto));
    }

    @Test
    void executeFails() throws InvalidInputException {
        Command loadMapTileCommand = commandFactory.create("name");
        when(mapTileReaderMock.load(any())).thenThrow(new InvalidInputException("errorMessage"));

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, failedWithMessage("errorMessage"));
    }

    @SuppressWarnings("unchecked")
    private MapMatrix<TilePrototype> mapMatrixMock() {
        return mock(MapMatrix.class);
    }
}