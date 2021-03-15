package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.controller.command.CommandHistory;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapObjectCommand;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(LoadMapObjectComponentTest.TestContextClone.class) // needs a separate context to start with an empty MapObjectRepository
public class LoadMapObjectComponentTest {

    @Autowired
    private LoadMapObjectCommand.Factory commandFactory;

    @Autowired
    private MapObjectRepository mapObjectRepository;

    @Autowired
    private CommandHistory commandHistory;

    @Test
    void loadMapObjectAndRollBack() {
        assertThat(mapObjectRepository.getKeys(), empty());

        String objectName = "tree2";
        CommandResult result = commandFactory.create(objectName).execute();

        assertThat(result, wasSuccessful());

        List<MapObjectRepository.ObjectKey> keys = mapObjectRepository.getKeys();
        assertThat(keys, hasSize(1));
        assertThat(mapObjectRepository.get(keys.get(0)).getName(), is(objectName));

        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        assertThat(mapObjectRepository.getKeys(), hasSize(0));
    }

    @Test
    void loadUnknownObject() {
        CommandResult result = commandFactory.create("unknown object").execute();

        assertThat(result, failedWithMessageContaining("Did not find resource for map object unknown object"));
    }

    @Configuration
    public static class TestContextClone extends TestManfreditorContext {
    }
}
