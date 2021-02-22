package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapCommand;
import manfred.manfreditor.map.MapModel;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static manfred.manfreditor.controller.command.CommandTestCase.assertCommandFailed;

@SpringJUnitConfig(TestManfreditorContext.class)
public class LoadMapComponentTest {

    @Autowired
    private LoadMapCommand.Factory commandFactory;

    @Autowired
    private MapModel mapModel;

    @Test
    void loadMap() {
        CommandResult commandResult = commandFactory.create("wald").execute();

        commandResult.onFailure(s -> { throw new AssertionFailedError("Loading map 'wald' failed: " + s); });
    }

    @Test
    void loadMapFails() {
        CommandResult commandResult = commandFactory.create("unknown map").execute();

        assertCommandFailed(commandResult, "Did not find resource for map unknown map");
    }
}
