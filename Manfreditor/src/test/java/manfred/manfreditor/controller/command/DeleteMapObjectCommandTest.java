package manfred.manfreditor.controller.command;

import manfred.manfreditor.gui.view.map.MapView;
import manfred.manfreditor.map.MapModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeleteMapObjectCommandTest {

    private DeleteMapObjectCommand.Factory commandFactory;
    private MapView mapViewMock;
    private MapModel mapModelMock;

    @BeforeEach
    void setUp() {
        mapViewMock = mock(MapView.class);
        mapModelMock = mock(MapModel.class);
        commandFactory = new DeleteMapObjectCommand.Factory(mapViewMock, mapModelMock);
    }

    @Test
    void failsWhenTileClickedCannotBeFound() {
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.empty());

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("No map tile at clicked coordinates (0,0) was found"));
    }

    @Test
    void failsWhenNoObjectToDelete() {
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.of(tileCoordinate(1, 2)));
        when(mapModelMock.deleteObjectAt(any())).thenReturn(false);

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage("No object found to delete at tile coordinate (1,2)"));
    }

    @Test
    void success() {
        when(mapViewMock.getClickedTile(anyInt(), anyInt())).thenReturn(Optional.of(tileCoordinate(1, 2)));
        when(mapModelMock.deleteObjectAt(any())).thenReturn(true);

        CommandResult result = commandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
    }
}