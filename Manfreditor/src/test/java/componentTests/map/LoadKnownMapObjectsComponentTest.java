package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.application.startup.LoadKnownMapObjectsCommand;
import manfred.manfreditor.map.model.mapobject.MapObjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

@SpringJUnitConfig(LoadKnownMapObjectsComponentTest.TestContextClone.class)
public class LoadKnownMapObjectsComponentTest {

    @Autowired
    private LoadKnownMapObjectsCommand underTest;

    @Autowired
    private MapObjectRepository mapObjectRepository;

    @Test
    void execute() {
        assertThat(mapObjectRepository.getKeys(), empty());

        CommandResult result = underTest.execute();

        assertThat(result, wasSuccessful());
        Optional<MapObjectRepository.ObjectKey> tree2Key = mapObjectRepository.getKeys().stream()
            .filter(objectKey -> objectKey.getKey().equals("tree2"))
            .findAny();
        assertThat(tree2Key.isPresent(), is(true));
        assertThat(mapObjectRepository.get(tree2Key.get()).getName(), is("tree2"));
    }

    @Configuration
    public static class TestContextClone extends TestManfreditorContext {
    }
}
