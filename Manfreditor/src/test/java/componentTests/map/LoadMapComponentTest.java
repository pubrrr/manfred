package componentTests.map;

import componentTests.ComponentTestCase;
import componentTests.TestManfreditorContext;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.LoadMapCommand;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.MapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(TestManfreditorContext.class)
public class LoadMapComponentTest extends ComponentTestCase {

    @Autowired
    private LoadMapCommand.Factory commandFactory;

    @Autowired
    private MapModel mapModel;

    @Autowired
    private CommandHistory commandHistory;

    @Test
    void loadMapAndRollBack() {
        loadMapsInDirectory(getClass().getResource("/map").getFile());

        assertThat(mapModel.getSizeX().value(), is(0));
        assertThat(mapModel.getSizeY().value(), is(0));

        MapRepository.MapKey mapKey = mapRepository.getKeys().find(key -> key.value.equals("Wald")).get();

        CommandResult commandResult = commandFactory.create(mapKey).execute();

        assertThat(commandResult, wasSuccessful());
        assertThat(mapModel.getFlattenedMap().getName(), is("Wald"));
        assertThat(mapModel.getSizeX().value(), greaterThan(1));
        assertThat(mapModel.getSizeY().value(), greaterThan(1));

        commandResult.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        assertThat(mapModel.getSizeX().value(), is(0));
        assertThat(mapModel.getSizeY().value(), is(0));
    }
}
