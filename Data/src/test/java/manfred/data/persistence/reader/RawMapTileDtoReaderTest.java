package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vavr.control.Try;
import manfred.data.persistence.dto.RawMapTileDto;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RawMapTileDtoReaderTest {

    private RawMapTileDtoReader underTest;
    private UrlHelper urlHelperMock;

    @BeforeEach
    void setUp() {
        urlHelperMock = mock(UrlHelper.class);
        underTest = new RawMapTileDtoReader(new ObjectMapper(new YAMLFactory()), mock(ImageLoader.class), urlHelperMock);
    }

    @Test
    void saveToFile() throws IOException {
        File yamlFile = createTempFile("test.yaml");
        when(urlHelperMock.getFileForMapTile(anyString())).thenReturn(yamlFile);
        File imageFile = createTempFile("test.png");
        when(urlHelperMock.getImageFileForMapTile(anyString())).thenReturn(imageFile);

        RawMapTileDto input = new RawMapTileDto("testName", List.of("1,0"), null, someImageData());

        Try<io.vavr.collection.List<File>> result = underTest.save(input);

        assertThat(result.isSuccess(), is(true));
        assertThat(result.get(), containsInAnyOrder(yamlFile, imageFile));
        assertThat(imageFile.isFile(), is(true));

        List<String> fileContents = Files.readAllLines(yamlFile.toPath());
        assertThat(fileContents.toString(), fileContents, hasItems("name: \"testName\"", "- \"1,0\""));
    }

    @Test
    void saveToFileFailsWhenYamlFileAlreadyExists() throws IOException {
        File yamlFile = createTempFile("test.yaml");
        Files.writeString(yamlFile.toPath(), "some content...");
        when(urlHelperMock.getFileForMapTile(anyString())).thenReturn(yamlFile);

        RawMapTileDto input = new RawMapTileDto("testName", List.of("1,0"), null, someImageData());

        Try<io.vavr.collection.List<File>> result = underTest.save(input);

        assertThat(result.isFailure(), is(true));
        assertThat(result.getCause().getMessage(), is("yaml file for tile testName already contains content"));
    }

    @Test
    void saveToFileFailsWhenImageFileAlreadyExists() throws IOException {
        File yamlFile = createTempFile("test.yaml");
        when(urlHelperMock.getFileForMapTile(anyString())).thenReturn(yamlFile);
        File imageFile = createTempFile("test.png");
        Files.writeString(imageFile.toPath(), "some content...");
        when(urlHelperMock.getImageFileForMapTile(anyString())).thenReturn(imageFile);

        RawMapTileDto input = new RawMapTileDto("testName", List.of("1,0"), null, someImageData());

        Try<io.vavr.collection.List<File>> result = underTest.save(input);

        assertThat(result.isFailure(), is(true));
        assertThat(result.getCause().getMessage(), is("image file for tile testName already contains content"));
    }

    private ImageData someImageData() {
        return new ImageData(1, 1, 1, new PaletteData(1, 1, 1));
    }

    private File createTempFile(String name) {
        String[] split = name.split("\\.");
        try {
            File tempFile = File.createTempFile(split[0], split[1]);
            tempFile.deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}