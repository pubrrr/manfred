package componentTests.map;

import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.LoadMapCommand;
import org.springframework.beans.factory.annotation.Autowired;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class ComponentTestCase {

    @Autowired
    protected LoadMapCommand.Factory loadMapCommandFactory;

    protected void loadMap(String mapName) {
        String file = getClass().getResource("/map/" + mapName + ".yaml").getFile();
        CommandResult loadMapResult = loadMapCommandFactory.create(file).execute();
        assertThat(loadMapResult, wasSuccessful());
    }
}
