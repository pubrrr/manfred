package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.controller.CommandFactory;
import manfred.manfreditor.controller.CommandResult;
import manfred.manfreditor.map.MapModel;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(TestManfreditorContext.class)
public class LoadMapComponentTest {

    @Autowired
    private CommandFactory commandFactory;

    @Autowired
    private MapModel mapModel;

    @Test
    void loadMap() {
        CommandResult commandResult = commandFactory.createLoadMapCommand("wald").execute();

        commandResult.onFailure(s -> { throw new AssertionFailedError("Loading map 'wald' failed: " + s); });
    }
}
