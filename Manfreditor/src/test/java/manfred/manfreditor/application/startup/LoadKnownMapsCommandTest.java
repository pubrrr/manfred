package manfred.manfreditor.application.startup;

import manfred.data.InvalidInputException;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapProvider;
import manfred.manfreditor.map.model.MapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadKnownMapsCommandTest {

    private LoadKnownMapsCommand underTest;
    private FileHelper fileHelperMock;
    private MapProvider mapProviderMock;
    private MapRepository mapRepositoryMock;

    @BeforeEach
    void setUp() {
        fileHelperMock = mock(FileHelper.class);
        mapProviderMock = mock(MapProvider.class);
        mapRepositoryMock = mock(MapRepository.class);
        underTest = new LoadKnownMapsCommand(fileHelperMock, mapProviderMock, mapRepositoryMock);
    }

    @Test
    void noFilesFound_null() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(null);

        CommandResult result = underTest.execute();

        assertThat(result, failedWithMessageContaining("no files found in directory"));
    }

    @Test
    void notFilesFound_empty() throws InvalidInputException {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[0]);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(mapProviderMock, never()).provide(any(MapSource.class));
    }

    @Test
    void mapProviderThrowsException() throws InvalidInputException {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{new File("path")});
        when(mapProviderMock.provide(any(MapSource.class))).thenThrow(new InvalidInputException("error message"));

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(mapRepositoryMock, never()).populateWith(any());
    }

    @Test
    void providedMapIsStoredInRepository() throws InvalidInputException {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{new File("path")});
        Map mapMock = mock(Map.class);
        when(mapProviderMock.provide(any(MapSource.class))).thenReturn(mapMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(mapRepositoryMock).populateWith(mapMock);
    }
}