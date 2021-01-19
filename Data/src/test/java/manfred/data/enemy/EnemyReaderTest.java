package manfred.data.enemy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import manfred.data.InvalidInputException;
import manfred.data.helper.UrlHelper;
import manfred.data.image.ImageLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class EnemyReaderTest {

    private EnemyReader underTest;
    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() {
        imageLoaderMock = mock(ImageLoader.class);

        underTest = new EnemyReader(imageLoaderMock, new ObjectMapper(new YAMLFactory()), new UrlHelper());
    }

    @Test
    void testConvert() throws InvalidInputException {
        URL url = getClass().getResource("/enemy/testMongo.yaml");
        EnemyDto result = underTest.load(url, url);

        assertThat(result.getName(), equalTo("Mongo"));
        assertThat(result.getHealthPoints(), equalTo(20));
        assertThat(result.getSpeed(), equalTo(5));
        verify(imageLoaderMock).load(any(URL.class));
    }

    @Test
    void unknownInputFile() {
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class, () -> underTest.load("unknown"));
        assertThat(exception.getMessage(), containsString("Resource for enemy unknown not found"));
    }
}