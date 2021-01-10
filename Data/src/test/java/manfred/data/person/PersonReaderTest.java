package manfred.data.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.InvalidInputException;
import manfred.data.image.ImageLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToObject;
import static org.mockito.Mockito.*;

class PersonReaderTest {
    private PersonReader underTest;
    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() {
        imageLoaderMock = mock(ImageLoader.class);

        underTest = new PersonReader(imageLoaderMock, new ObjectMapper(new YAMLFactory()));
    }

    @Test
    void invalidFileContent() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.load("unknown"));
        assertThat(exception.getMessage(), containsString("Resource for '/persons/unknown.yaml' no found"));
    }

    @Test
    void convertsName() throws InvalidInputException {
        BufferedImage imageMock = mock(BufferedImage.class);
        when(imageLoaderMock.load(any(URL.class))).thenReturn(imageMock);

        URL url = getClass().getResource("/persons/testOpa.yaml");
        PersonDto result = underTest.load(url, getClass().getResource("/persons/testOpa.yaml"));

        assertThat(result.getName(), equalToObject("Opa"));
        assertThat(result.getImage(), equalToObject(imageMock));
    }
}