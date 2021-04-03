package manfred.manfreditor.newmapobject.controller.command;

import io.vavr.control.Option;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel.PreviewTileCoordinate;
import manfred.manfreditor.newmapobject.view.NewMapObjectView;
import org.eclipse.swt.graphics.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClickObjectPreviewCommandTest {

    private ClickObjectPreviewCommand.Factory commandFactory;
    private NewMapObjectView newMapObjectViewMock;
    private NewMapObjectModel newMapObjectModelMock;
    private CommandHistory commandHistory;

    @BeforeEach
    void setUp() {
        commandHistory = new CommandHistory();
        newMapObjectViewMock = mock(NewMapObjectView.class);
        newMapObjectModelMock = mock(NewMapObjectModel.class);
        commandFactory = new ClickObjectPreviewCommand.Factory(newMapObjectViewMock, newMapObjectModelMock);
    }

    @Test
    void noTileClicked_thenModelReceivesNothing() {
        when(newMapObjectViewMock.getClickedTile(anyInt(), anyInt(), any())).thenReturn(Option.none());

        CommandResult result = commandFactory.create(0, 0, new Point(0, 0)).execute();

        assertThat(result, failedWithMessageContaining("no tile clicked"));
        verify(newMapObjectModelMock, never()).invertAccessibility(any());
    }

    @Test
    void tileClicked_thenAccessibilityInInverted() {
        when(newMapObjectViewMock.getClickedTile(anyInt(), anyInt(), any())).thenReturn(Option.some(someTileCoordinate()));

        CommandResult result = commandFactory.create(0, 0, new Point(0, 0)).execute();

        assertThat(result, wasSuccessful());
        verify(newMapObjectModelMock, times(1)).invertAccessibility(eq(someTileCoordinate()));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();

        verify(newMapObjectModelMock, times(2)).invertAccessibility(eq(someTileCoordinate()));
    }

    private PreviewTileCoordinate someTileCoordinate() {
        return new PreviewTileCoordinate(0, 0);
    }
}