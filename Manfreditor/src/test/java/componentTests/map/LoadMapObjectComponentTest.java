package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapObjectCommand;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessage;
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

    @Test
    void loadMapObject() {
        assertThat(mapObjectRepository.getKeys(), empty());

        String objectName = "tree2";
        CommandResult result = commandFactory.create(objectName).execute();

        result.onFailure(s -> { throw new AssertionFailedError("Loading map object for " + objectName + " failed: " + s); });

        List<MapObjectRepository.ObjectKey> keys = mapObjectRepository.getKeys();
        assertThat(keys, hasSize(1));
        assertThat(mapObjectRepository.get(keys.get(0)).getName(), is(objectName));
    }

    @Test
    void loadUnknownObject() {
        CommandResult result = commandFactory.create("unknown object").execute();

        assertThat(result, failedWithMessage("Did not find resource for map object unknown object"));
    }

    @Configuration
    public static class TestContextClone extends TestManfreditorContext {
    }
}
