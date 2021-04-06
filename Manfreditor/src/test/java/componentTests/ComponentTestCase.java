package componentTests;

import manfred.manfreditor.application.startup.LoadKnownMapsCommand;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.LoadMapCommand;
import manfred.manfreditor.map.model.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public abstract class ComponentTestCase {

    @Autowired
    protected LoadMapCommand.Factory loadMapCommandFactory;

    @Autowired
    protected MapRepository mapRepository;

    @Autowired
    protected FileHelper fileHelperMock;

    @Autowired
    protected LoadKnownMapsCommand loadKnownMapsCommand;

    protected void loadMap(String mapName) {
        loadMapsInDirectory(getClass().getResource("/map").getFile());

        CommandResult commandResult = mapRepository.getKeys()
            .find(key -> key.value.equals(mapName))
            .map(matchingKey -> loadMapCommandFactory.create(matchingKey).execute())
            .getOrElse(CommandResult.failure("Did not find map " + mapName + " in repository"));
        assertThat(commandResult, wasSuccessful());
    }

    protected void loadMapsInDirectory(String directory) {
        loadMapsInDirectory(new File(directory));
    }

    protected void loadMapsInDirectory(File directory) {
        when(fileHelperMock.getFilesIn(any())).thenReturn(directory.listFiles());
        loadKnownMapsCommand.execute();
    }
}
