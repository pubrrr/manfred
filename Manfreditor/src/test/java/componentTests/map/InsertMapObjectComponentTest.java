package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.controller.command.CommandHistory;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.InsertMapObjectCommand;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.None;
import manfred.manfreditor.mapobject.SelectedObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.CoordinateHelper.tileCoordinate;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(InsertMapObjectComponentTest.TestContextClone.class)
public class InsertMapObjectComponentTest extends ComponentTestCase {

    @Autowired
    private InsertMapObjectCommand.Factory underTestCommandFactory;

    @Autowired
    private MapObjectRepository mapObjectRepository;

    @Autowired
    private SelectedObject selectedObject;

    @Autowired
    private MapModel mapModel;

    @Autowired
    private CommandHistory commandHistory;

    @Test
    void insertSomethingInEmptyMapIsSuccessful() {
        insertSomethingInEmptyMap();
    }

    @Test
    void insertSomethingInNonEmptyMapFails() {
        loadMap("nonEmptyMap");
        selectSomeObject();

        CommandResult result = underTestCommandFactory.create(0, 0).execute();

        assertThat(result, failedWithMessage(
            "Tile (0,0) is not accessible, blocked by object tree2 at (0,0),\n" +
            "Tile (1,0) is not accessible, blocked by object tree2 at (0,0)"
        ));
    }

    @Test
    void rollback() {
        CommandResult result = insertSomethingInEmptyMap();

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        MapObject mapObject = mapModel.getObjects().get(tileCoordinate(0, 2));
        assertThat(mapObject, instanceOf(None.class));
    }

    private CommandResult insertSomethingInEmptyMap() {
        loadMap("emptyMap");
        selectSomeObject();

        CommandResult result = underTestCommandFactory.create(0, 0).execute();

        assertThat(result, wasSuccessful());
        MapObject mapObject = mapModel.getObjects().get(tileCoordinate(0, 2));
        assertThat(mapObject, instanceOf(ConcreteMapObject.class));
        assertThat(((ConcreteMapObject) mapObject).getName(), is("tree2"));
        return result;
    }

    private void selectSomeObject() {
        MapObjectRepository.ObjectKey someObjectKey = mapObjectRepository.getKeys().get(0);
        if (!selectedObject.isSelected(someObjectKey)) {
            selectedObject.select(someObjectKey);
        }
    }

    @Configuration
    public static class TestContextClone extends TestManfreditorContext {
    }
}
