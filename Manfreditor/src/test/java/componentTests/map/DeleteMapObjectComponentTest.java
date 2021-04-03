package componentTests.map;

import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.DeleteMapObjectCommand;
import manfred.manfreditor.map.controller.command.LoadMapCommand;
import manfred.manfreditor.map.view.map.MapView;
import manfred.manfreditor.map.view.map.TileViewSize;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import manfred.manfreditor.map.model.mapobject.MapObject;
import manfred.manfreditor.map.model.mapobject.None;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(InsertMapObjectComponentTest.TestContextClone.class)
public class DeleteMapObjectComponentTest extends ComponentTestCase {

    @Autowired
    private DeleteMapObjectCommand.Factory underTestCommandFactory;

    @Autowired
    private LoadMapCommand.Factory loadMapCommandFactory;

    @Autowired
    private MapModel mapModel;

    @Autowired
    private MapView mapView;

    @Autowired
    private CommandHistory commandHistory;

    @Test
    void deleteObjectAtItsSourceAndRollback() {
        loadMap("nonEmptyMap");
        assertMapIsSetupAsExpected();

        CommandResult result = underTestCommandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        MapObject mapObjectAfterDeletion = mapModel.getObjects().get(tileCoordinate(0, 0)).get();
        assertThat(mapObjectAfterDeletion, instanceOf(None.class));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        MapObject mapObjectAfterRollback = mapModel.getObjects().get(tileCoordinate(0, 0)).get();
        assertThat(mapObjectAfterRollback, instanceOf(ConcreteMapObject.class));
        assertThat(((ConcreteMapObject) mapObjectAfterRollback).getName(), is("tree2"));
    }

    @Test
    void deleteObjectByClickingAnotherOfItsTiles() {
        loadMap("nonEmptyMap");
        assertMapIsSetupAsExpected();

        assertThat(mapView.getClickedTile(TileViewSize.TILE_SIZE, 0), is(Optional.of(tileCoordinate(1, 0))));
        CommandResult result = underTestCommandFactory.create(TileViewSize.TILE_SIZE, 0).execute();

        assertThat(result, wasSuccessful());
        MapObject mapObjectAfterDeletion = mapModel.getObjects().get(tileCoordinate(0, 0)).get();
        assertThat(mapObjectAfterDeletion, instanceOf(None.class));
    }

    @Test
    void deleteNonExistentObjectFails() {
        loadMap("emptyMap");

        MapObject clickedMapObject = mapModel.getObjects().get(tileCoordinate(0, 0)).get();
        assertThat(clickedMapObject, instanceOf(None.class));

        CommandResult result = underTestCommandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessageContaining("No object could be deleted at tile (0,2)"));
        MapObject mapObjectAfterDeletion = mapModel.getObjects().get(tileCoordinate(0, 0)).get();
        assertThat(mapObjectAfterDeletion, instanceOf(None.class));
    }

    private void assertMapIsSetupAsExpected() {
        MapObject mapObject = mapModel.getObjects().get(tileCoordinate(0, 0)).get();
        assertThat(mapObject, instanceOf(ConcreteMapObject.class));
        assertThat(((ConcreteMapObject) mapObject).getName(), is("tree2"));

        MapObject otherMapObject = mapModel.getObjects().get(tileCoordinate(1, 0)).get();
        assertThat(otherMapObject, instanceOf(None.class));
    }
}
