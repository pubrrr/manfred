package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.LoadMapCommand;
import manfred.manfreditor.map.model.MapModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.net.MalformedURLException;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(TestManfreditorContext.class)
public class LoadMapComponentTest {

    @Autowired
    private LoadMapCommand.Factory commandFactory;

    @Autowired
    private MapModel mapModel;

    @Autowired
    private CommandHistory commandHistory;

    @Test
    void loadMapAndRollBack() {
        String file = getClass().getResource("/map/wald.yaml").getFile();
        assertThat(mapModel.getSizeX().value(), is(0));
        assertThat(mapModel.getSizeY().value(), is(0));

        CommandResult commandResult = commandFactory.create(file).execute();

        assertThat(commandResult, wasSuccessful());
        assertThat(mapModel.getSizeX().value(), greaterThan(1));
        assertThat(mapModel.getSizeY().value(), greaterThan(1));

        commandResult.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        assertThat(mapModel.getSizeX().value(), is(0));
        assertThat(mapModel.getSizeY().value(), is(0));
    }

    @Test
    void loadMapFails() throws MalformedURLException {
        String nonExistent = new File("nonExistent").toURI().toURL().getFile();

        CommandResult commandResult = commandFactory.create(nonExistent).execute();

        assertThat(commandResult, failedWithMessageContaining("Could not read map from file:" + nonExistent));
    }
}
