package componentTests.startup;

import io.vavr.control.Option;
import manfred.manfreditor.application.ManfreditorContext;
import manfred.manfreditor.application.startup.LoadKnownMapsCommand;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.model.MapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(LoadKnownMapsComponentTest.TestContextClone.class)
public class LoadKnownMapsComponentTest {

    @Autowired
    private LoadKnownMapsCommand loadKnownMapsCommand;

    @Autowired
    private MapRepository mapRepository;

    @Test
    void load() {
        assertThat(mapRepository.getKeys().size(), is(0));

        CommandResult result = loadKnownMapsCommand.execute();

        assertThat(result, wasSuccessful());
        Option<MapRepository.MapKey> waldKey = mapRepository.getKeys().find(key -> key.value.equals("Wald"));
        assertThat(waldKey.isDefined(), is(true));
        assertThat(mapRepository.get(waldKey.get()).getName(), is("Wald"));
    }

    @Configuration
    public static class TestContextClone extends ManfreditorContext {
    }
}
