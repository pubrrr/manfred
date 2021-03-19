package manfred.manfreditor.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class FileWriterTest {

    private FileWriter underTest;

    @BeforeEach
    void setUp() {
        underTest = new FileWriter();
    }

    @Test
    void write() throws IOException {
        File temporaryFile = File.createTempFile("testMap", ".yaml");
        temporaryFile.deleteOnExit();

        underTest.write(temporaryFile, "content");

        String fileContents = Files.readString(temporaryFile.toPath());
        assertThat(fileContents, is("content"));
    }
}