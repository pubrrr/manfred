package manfred.manfreditor.controller.command;

import io.vavr.collection.List;
import io.vavr.control.Try;
import manfred.manfreditor.mapobject.NewMapObjectData;
import manfred.manfreditor.mapobject.export.MapObjectExporter;
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
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapObjectExporterMock = mock(MapObjectExporter.class);
        commandFactory = new CreateMapObjectCommand.Factory(mapObjectExporterMock);
    }

    @Test
    void create() {
        var input = new NewMapObjectData("name", null, null);
        File saveFileMock = mock(File.class);
        when(mapObjectExporterMock.export(any())).thenReturn(Try.success(List.of(saveFileMock)));

        CommandResult result = commandFactory.create(input).execute();

        assertThat(result, wasSuccessful());
        result.registerRollbackOperation(commandHistory);

        commandHistory.undoLast();
        verify(saveFileMock).delete();
    }

    @Test
    void savingFilesFails() {
        var input = new NewMapObjectData("name", null, null);
        File saveFileMock = mock(File.class);
        when(mapObjectExporterMock.export(any())).thenReturn(Try.failure(new Exception("error message")));

        CommandResult result = commandFactory.create(input).execute();

        assertThat(result, failedWithMessageContaining("error message"));
    }
}