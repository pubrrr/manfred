package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapCommand;
import manfred.manfreditor.map.MapModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.net.MalformedURLException;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringJUnitConfig(TestManfreditorContext.class)
public class LoadMapComponentTest {

    @Autowired
    private LoadMapCommand.Factory commandFactory;

    @Autowired
    private MapModel mapModel;

    @Test
    void loadMap() {
        String file = getClass().getResource("/map/wald.yaml").getFile();

        CommandResult commandResult = commandFactory.create(file).execute();

        assertThat(commandResult, wasSuccessful());
    }

    @Test
    void loadMapFails() throws MalformedURLException {
        String nonExistent = new File("nonExistent").toURI().toURL().getFile();

        CommandResult commandResult = commandFactory.create(nonExistent).execute();

        assertThat(commandResult, failedWithMessage("Could not read map from file:" + nonExistent));
    }
}
