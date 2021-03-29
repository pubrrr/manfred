package manfred.manfreditor.controller.newmapobject;

import manfred.manfreditor.controller.command.CommandHistory;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.mapobject.NewMapObjectModel;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoadObjectImageCommandTest {

    private LoadObjectImageCommand.Factory commandFactory;
    private ImageLoader imageLoaderMock;
    private NewMapObjectModel newMapObjectModel;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        imageLoaderMock = mock(ImageLoader.class);
        newMapObjectModel = mock(NewMapObjectModel.class);
        commandFactory = new LoadObjectImageCommand.Factory(imageLoaderMock, newMapObjectModel);
    }

    @Test
    void noValidFileGiven() {
        when(imageLoaderMock.load(anyString())).thenThrow(new SWTError("error message"));

        CommandResult result = commandFactory.create("notAValidFile").execute();

        assertThat(result, failedWithMessageContaining("error message"));
    }

    @Test
    void validFileGiven_thenImageStoredInModel() {
        ImageData imageData1 = someImageData();
        ImageData imageData2 = someImageData();
        when(imageLoaderMock.load(anyString())).thenReturn(new ImageData[]{imageData1, imageData2});
        ImageData previousImageData = someImageData();
        when(newMapObjectModel.getImageData()).thenReturn(Optional.of(previousImageData));

        CommandResult result = commandFactory.create("notAValidFile").execute();

        assertThat(result, wasSuccessful());
        verify(newMapObjectModel).setImageData(eq(imageData1));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        verify(newMapObjectModel).setImageData(eq(previousImageData));
    }

    private ImageData someImageData() {
        return new ImageData(1, 1, 1, new PaletteData(0, 0, 0));
    }
}