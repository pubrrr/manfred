package manfred.manfreditor.controller.command.startup;

import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.controller.command.Command;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapObjectCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadKnownMapObjectsCommandTest {

    private LoadKnownMapObjectsCommand underTest;
    private LoadMapObjectCommand.Factory loadMapObjectCommandFactoryMock;
    private FileHelper fileHelperMock;

    @BeforeEach
    void setUp() {
        loadMapObjectCommandFactoryMock = mock(LoadMapObjectCommand.Factory.class);
        fileHelperMock = mock(FileHelper.class);
        underTest = new LoadKnownMapObjectsCommand(loadMapObjectCommandFactoryMock, fileHelperMock);
    }

    @Test
    void filesInDirectoryYieldsNull() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(null);

        CommandResult result = underTest.execute();

        assertThat(result, failedWithMessageContaining("no files found in directory"));
    }

    @Test
    void noFilesInDirectory() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{});

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(loadMapObjectCommandFactoryMock, never()).create(any(File.class), any(File.class));
    }

    @Test
    void aPngAndAYamlWithSameNameInDirectory_isLoaded() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{
            new File("name.yaml"),
            new File("name.png")
        });
        Command commandMock = mock(Command.class);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(commandMock, atLeastOnce()).execute();
    }

    @Test
    void aPngAndSomeOtherFileWithSameNameInDirectory_isIgnored() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{
            new File("name.unknownExtension"),
            new File("name.png")
        });
        Command commandMock = mock(Command.class);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(commandMock, never()).execute();
    }

    @Test
    void aYamlAndSomeOtherFileWithSameNameInDirectory_isIgnored() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{
            new File("name.unknownExtension"),
            new File("name.yaml")
        });
        Command commandMock = mock(Command.class);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(commandMock, never()).execute();
    }

    @Test
    void onlyOneFileInDirectory_isIgnored() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{
            new File("name.yaml")
        });
        Command commandMock = mock(Command.class);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(commandMock, never()).execute();
    }

    @Test
    void twoValidPairsOfFilesInDirectory() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{
            new File("name.yaml"),
            new File("name.png"),
            new File("otherName.yaml"),
            new File("otherName.png")
        });
        Command commandMock = mock(Command.class);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(commandMock, times(2)).execute();
    }

    @Test
    void pngYamlAndAnotherFileWithSameNameInDirectory_otherFileIsIgnored() {
        when(fileHelperMock.getFilesIn(any())).thenReturn(new File[]{
            new File("name.yaml"),
            new File("name.png"),
            new File("name.other"),
        });
        Command commandMock = mock(Command.class);
        when(loadMapObjectCommandFactoryMock.create(any(File.class), any(File.class))).thenReturn(commandMock);

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        verify(commandMock, atLeastOnce()).execute();
    }
}