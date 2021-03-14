package manfred.data.persistence.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vavr.control.Option;
import io.vavr.control.Try;
import manfred.data.InvalidInputException;
import manfred.data.persistence.PreviousFileContent;
import manfred.data.persistence.dto.RawMapDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

class RawMapReaderTest {

    private RawMapReader underTest;
    private File temporaryFile;

    @BeforeEach
    void init() throws IOException {
        underTest = new RawMapReader(new ObjectMapper(new YAMLFactory()), new UrlHelper());
        temporaryFile = File.createTempFile("testMap", "yaml");
        temporaryFile.deleteOnExit();
    }

    @Test
    void testConvert() throws InvalidInputException {
        URL url = getClass().getResource("/maps/testMap.yaml");
        RawMapDto result = underTest.load(url);

        assertThat(result.getName(), equalTo("Wald"));
        assertThat(result.getMap(), hasSize(2));
        assertThat(result.getPortals(), hasSize(1));
        assertThat(result.getDoors(), hasSize(2));
        assertThat(result.getEnemies(), hasSize(1));
    }

    @Test
    void unknownInputFile() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.load("unknown"));
        assertThat(exception.getMessage(), containsString("Did not find resource for map"));
    }

    @Test
    void overrideFile_thenOldFileContentIsReturned() throws IOException {
        var input = new RawMapDto();
        input.setName("name");
        FileWriter fileWriter = new FileWriter(temporaryFile);
        fileWriter.write("previous file content");
        fileWriter.close();

        Try<Option<PreviousFileContent>> result = underTest.save(input, temporaryFile.toURI().toURL());

        assertThat(result.isSuccess(), is(true));
        assertThat(result.get().get().getContent(), is("previous file content"));
        List<String> fileContents = Files.readAllLines(temporaryFile.toPath());
        assertThat(fileContents.toString(), fileContents, hasItem("name: \"name\""));
    }

    @Test
    void saveInNewFile() throws IOException {
        var input = new RawMapDto();
        input.setName("name");

        Try<Option<PreviousFileContent>> result = underTest.save(input, temporaryFile.toURI().toURL());

        assertThat(result.isSuccess(), is(true));
        assertThat(result.get().isEmpty(), is(true));
        List<String> fileContents = Files.readAllLines(temporaryFile.toPath());
        assertThat(fileContents.toString(), fileContents, hasItem("name: \"name\""));
    }

    @Test
    void saveFails() throws IOException {
        var input = new RawMapDto();
        input.setName("name");

        Try<Option<PreviousFileContent>> result = underTest.save(input, new URL("http://some.unknown.file"));

        assertThat(result.isFailure(), is(true));
        assertThat(result.getCause(), instanceOf(IOException.class));
        List<String> fileContents = Files.readAllLines(temporaryFile.toPath());
        assertThat(fileContents.toString(), fileContents, empty());
    }
}