package manfred.manfreditor.controller.command;

import io.vavr.collection.List;
import io.vavr.control.Try;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import manfred.manfreditor.newmapobject.model.export.MapObjectExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateMapObjectCommandTest {

    private CreateMapObjectCommand.Factory commandFactory;
    private MapObjectExporter mapObjectExporterMock;
    private LoadMapObjectCommand.Factory loadMapObjectCommandFactoryMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapObjectExporterMock = mock(MapObjectExporter.class);
        loadMapObjectCommandFactoryMock = mock(LoadMapObjectCommand.Factory.class);
        commandFactory = new CreateMapObjectCommand.Factory(mapObjectExporterMock, loadMapObjectCommandFactoryMock);
    }

    @Test
    void create() {
        var input = new NewMapObjectData("name", null, null);
        File yamlFileMock = mockFile("test.yaml");
        File imageFileMock = mockFile("test.png");
        when(mapObjectExporterMock.export(any())).thenReturn(Try.success(List.of(yamlFileMock, imageFileMock)));

        RollbackOperation rollbackOperationMock = mock(RollbackOperation.class);
        Command commandMock = () -> CommandResult.success(rollbackOperationMock);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = commandFactory.create(input).execute();

        assertThat(result, wasSuccessful());
        result.registerRollbackOperation(commandHistory);

        commandHistory.undoLast();
        verify(yamlFileMock).delete();
        verify(imageFileMock).delete();
        verify(rollbackOperationMock).rollback();
    }

    @Test
    void loadingCreatedFilesFails() {
        var input = new NewMapObjectData("name", null, null);
        File yamlFileMock = mockFile("test.yaml");
        File imageFileMock = mockFile("test.png");
        when(mapObjectExporterMock.export(any())).thenReturn(Try.success(List.of(yamlFileMock, imageFileMock)));

        Command failingCommand = () -> CommandResult.failure("loading files faild error message");
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(failingCommand);

        CommandResult result = commandFactory.create(input).execute();

        assertThat(result, failedWithMessageContaining("loading files faild error message"));
        verify(yamlFileMock).delete();
        verify(imageFileMock).delete();
    }

    @Test
    void savingFilesFails() {
        var input = new NewMapObjectData("name", null, null);
        File saveFileMock = mock(File.class);
        when(mapObjectExporterMock.export(any())).thenReturn(Try.failure(new Exception("error message")));

        CommandResult result = commandFactory.create(input).execute();

        assertThat(result, failedWithMessageContaining("error message"));
    }

    private File mockFile(String fileName) {
        File fileMock = mock(File.class);
        when(fileMock.getName()).thenReturn(fileName);
        return fileMock;
    }
}