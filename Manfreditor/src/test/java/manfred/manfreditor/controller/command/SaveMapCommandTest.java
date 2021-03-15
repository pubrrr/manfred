package manfred.manfreditor.controller.command;

import io.vavr.control.Option;
import io.vavr.control.Try;
import manfred.data.persistence.PreviousFileContent;
import manfred.manfreditor.common.FileWriter;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.map.export.MapExporter;
import manfred.manfreditor.map.flattened.FlattenedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SaveMapCommandTest {

    private SaveMapCommand.Factory commandFactory;
    private MapModel mapModelMock;
    private MapExporter mapExporterMock;
    private FileWriter fileWriterMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        mapModelMock = mock(MapModel.class);
        mapExporterMock = mock(MapExporter.class);
        fileWriterMock = mock(FileWriter.class);
        commandFactory = new SaveMapCommand.Factory(mapModelMock, mapExporterMock, fileWriterMock);
    }

    @Test
    void saveMap_noPreviousFileContentReturned() {
        FlattenedMap mapToSave = mock(FlattenedMap.class);
        when(mapModelMock.getFlattenedMap()).thenReturn(mapToSave);
        File fileToSaveIn = mock(File.class);

        when(mapExporterMock.export(any(), any())).thenReturn(Try.success(Option.none()));

        CommandResult result = commandFactory.create(fileToSaveIn, null).execute();

        assertThat(result, wasSuccessful());
        verify(mapExporterMock).export(eq(mapToSave), eq(fileToSaveIn));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(fileToSaveIn).delete();
    }

    @Test
    void saveMap_fileWasOverritten_thenPreviousFileContentIsReturned() {
        FlattenedMap mapToSave = mock(FlattenedMap.class);
        when(mapModelMock.getFlattenedMap()).thenReturn(mapToSave);
        File fileToSaveIn = mock(File.class);

        when(mapExporterMock.export(any(), any())).thenReturn(
            Try.success(Option.some(new PreviousFileContent("previous content")))
        );
        when(fileWriterMock.write(any(), any())).thenReturn(Try.success(null));

        CommandResult result = commandFactory.create(fileToSaveIn, null).execute();

        assertThat(result, wasSuccessful());
        verify(mapExporterMock).export(eq(mapToSave), eq(fileToSaveIn));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(fileWriterMock).write(eq(fileToSaveIn), eq("previous content"));
    }

    @Test
    void saveMapFails() {
        FlattenedMap mapToSave = mock(FlattenedMap.class);
        when(mapModelMock.getFlattenedMap()).thenReturn(mapToSave);
        File fileToSaveIn = mock(File.class);

        when(mapExporterMock.export(any(), any())).thenReturn(Try.failure(new Exception("message")));

        CommandResult result = commandFactory.create(fileToSaveIn, null).execute();

        assertThat(result, failedWithMessageContaining("message"));
    }
}