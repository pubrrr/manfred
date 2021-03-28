package manfred.manfreditor.controller.command;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.MapTileReader;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.persistence.reader.MapTileSource;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
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
        when(mapTileReaderMock.load(any(MapTileSource.class))).thenReturn(validatedMapTileDto);

        Command loadMapTileCommand = commandFactory.create(someValidYamlFile(), someValidPngFile());

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, wasSuccessful());
        verify(mapObjectRepositoryMock).populateWith(eq(validatedMapTileDto));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(mapObjectRepositoryMock).delete(any());
    }

    private String someValidPngFile() {
        return getClass().getResource("/mapObject/tree2.png").getFile();
    }

    @Test
    void failsWhenYamlFileIsNotAFile() {
        Command loadMapTileCommand = commandFactory.create("invalidFilePath", "invalidImageFilePath");

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, failedWithMessageContaining("invalidFilePath is not a yaml file"));
    }

    @Test
    void failsWhenImageFileIsNotAFile() {
        Command loadMapTileCommand = commandFactory.create(someValidYamlFile(), "invalidImageFilePath");

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, failedWithMessageContaining("invalidImageFilePath is not a png file"));
    }

    @Test
    void failsWhenReadingFilesFails() throws InvalidInputException {
        when(mapTileReaderMock.load(any(MapTileSource.class))).thenThrow(new InvalidInputException("errorMessage"));
        Command loadMapTileCommand = commandFactory.create(someValidYamlFile(), someValidPngFile());

        CommandResult result = loadMapTileCommand.execute();

        assertThat(result, failedWithMessageContaining("errorMessage"));
    }

    private String someValidYamlFile() {
        return getClass().getResource("/mapObject/tree2.yaml").getFile();
    }

    private MapPrototype mapMatrixMock() {
        return mock(MapPrototype.class);
    }
}