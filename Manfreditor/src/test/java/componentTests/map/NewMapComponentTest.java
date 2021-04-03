package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.NewMapCommand;
import manfred.manfreditor.map.model.MapModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(NewMapComponentTest.TestContextClone.class)
public class NewMapComponentTest {

    @Autowired
    private NewMapCommand.Factory commandFactory;

    @Autowired
    private MapModel mapModel;

    @Autowired
    private CommandHistory commandHistory;

    @Test
    void createNewMap() {
        assertThat(mapModel.getSizeX().value(), is(0));
        assertThat(mapModel.getSizeY().value(), is(0));

        CommandResult commandResult = commandFactory.create("newName", 5, 7).execute();

        assertThat(commandResult, wasSuccessful());
        assertThat(mapModel.getFlattenedMap().getName(), is("newName"));
        assertThat(mapModel.getSizeX().value(), is(5));
        assertThat(mapModel.getSizeY().value(), is(7));

        commandResult.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        assertThat(mapModel.getSizeX().value(), is(0));
        assertThat(mapModel.getSizeY().value(), is(0));
    }

    @Configuration
    public static class TestContextClone extends TestManfreditorContext {
    }
}
