package componentTests.map;

import componentTests.TestManfreditorContext;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.controller.command.CommandHistory;
import manfred.manfreditor.controller.command.CommandResult;
import manfred.manfreditor.controller.command.LoadMapCommand;
import manfred.manfreditor.controller.command.SaveMapCommand;
import manfred.manfreditor.gui.PopupProvider;
import org.eclipse.swt.SWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static manfred.manfreditor.helper.CommandFailedMatcher.failedWithMessageContaining;
import static manfred.manfreditor.helper.SuccessfulCommandMatcher.wasSuccessful;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(SaveMapComponentTest.TestContextClone.class)
public class SaveMapComponentTest extends ComponentTestCase {

    @Autowired
    private SaveMapCommand.Factory commandFactory;

    @Autowired
    private LoadMapCommand.Factory loadMapCommandFactory;

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private PopupProvider popupProviderMock;

    private CommandHistory commandHistory;

    private File temporaryFile;

    @BeforeEach
    void setUp() throws IOException {
        commandHistory = new CommandHistory();

        temporaryFile = File.createTempFile(
            "testMap",
            ".yaml",
            new File(getClass().getResource("/map/emptyMap.yaml").getFile()).getParentFile()
        );
        temporaryFile.deleteOnExit();
    }

    @Test
    void saveMapToNewFile() throws IOException {
        loadAMap();

        CommandResult result = commandFactory.create(temporaryFile, null).execute();

        assertThat(result, wasSuccessful());
        assertFileContainsExpectedContent();

        when(popupProviderMock.showConfirmationDialog(any(), any())).thenReturn(SWT.YES);
        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        assertThat(temporaryFile.exists(), is(false));
    }

    @Test
    void saveMapToExistingFileWithContent() throws IOException {
        loadAMap();
        fileHelper.write(temporaryFile, "previous content");
        assertFileContains("previous content");

        CommandResult result = commandFactory.create(temporaryFile, null).execute();

        assertThat(result, wasSuccessful());
        assertFileContainsExpectedContent();

        when(popupProviderMock.showConfirmationDialog(any(), any())).thenReturn(SWT.YES);
        result.registerRollbackOperation(commandHistory);
        commandHistory.undoLast();
        assertFileContains("previous content");
    }

    @Test
    void saveMapFails() {
        temporaryFile.setReadOnly();

        CommandResult result = commandFactory.create(temporaryFile, null).execute();

        assertThat(result, failedWithMessageContaining("testMap"));
    }

    private void loadAMap() {
        loadMap("nonEmptyMap");
    }

    private void assertFileContainsExpectedContent() throws IOException {
        assertFileContains(
            "---\n" +
            "name: \"nonEmptyTestMap\"\n" +
            "map:\n" +
            "- \"tree2,_tree2\"\n" +
            "persons: []\n" +
            "portals: []\n" +
            "doors: []\n" +
            "enemies: []\n"
        );
    }

    private void assertFileContains(String content) throws IOException {
        String fileContents = Files.readString(temporaryFile.toPath());
        assertThat(fileContents, is(content));
    }

    @Configuration
    public static class TestContextClone extends TestManfreditorContext {

        @Bean
        public PopupProvider popupProvider() {
            return mock(PopupProvider.class);
        }
    }
}
