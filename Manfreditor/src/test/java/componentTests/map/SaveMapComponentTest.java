package componentTests.map;

import componentTests.ComponentTestCase;
import componentTests.TestManfreditorContext;
import manfred.data.persistence.reader.MapSource;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.common.FileHelper;
import manfred.manfreditor.common.PopupProvider;
import manfred.manfreditor.common.command.CommandHistory;
import manfred.manfreditor.common.command.CommandResult;
import manfred.manfreditor.map.controller.command.LoadMapCommand;
import manfred.manfreditor.map.controller.command.SaveMapCommand;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.MapModel;
import manfred.manfreditor.map.model.ObjectInsertionValidator;
import manfred.manfreditor.map.model.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
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

    private final FileHelper fileHelper = new FileHelper();

    @Autowired
    private PopupProvider popupProviderMock;

    private CommandHistory commandHistory;

    private static File temporaryFile;

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

        CommandResult result = commandFactory.create(null).execute();

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

        CommandResult result = commandFactory.create(null).execute();

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

        CommandResult result = commandFactory.create(null).execute();

        assertThat(result, failedWithMessageContaining("testMap"));
    }

    private void loadAMap() {
        loadMap("nonEmptyMap");
    }

    private void assertFileContainsExpectedContent() throws IOException {
        assertFileContains(
            "---\n" +
            "name: \"nonEmptyMap\"\n" +
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
        public FileHelper fileHelper() {
            FileHelper mock = mock(FileHelper.class);
            when(mock.write(any(), any())).thenCallRealMethod();
            return mock;
        }

        @Bean
        public PopupProvider popupProvider() {
            return mock(PopupProvider.class);
        }

        @Bean
        public MapModel mapModel(AccessibilityMerger accessibilityMerger, ObjectInsertionValidator objectInsertionValidator) {
            return new MapModelDouble(new Map("test", PositiveInt.of(0), PositiveInt.of(0), new MapSource(new File(""))), accessibilityMerger, objectInsertionValidator);
        }
    }

    private static class MapModelDouble extends MapModel {

        public MapModelDouble(Map initialMap, AccessibilityMerger accessibilityMerger, ObjectInsertionValidator objectInsertionValidator) {
            super(initialMap, accessibilityMerger, objectInsertionValidator);
        }

        @Override
        public FlattenedMap getFlattenedMap() {
            FlattenedMap actualFlatMap = super.getFlattenedMap();
            return new FlattenedMap(actualFlatMap.getName(), actualFlatMap.getAccessibility(), new MapSource(SaveMapComponentTest.temporaryFile));
        }
    }
}
