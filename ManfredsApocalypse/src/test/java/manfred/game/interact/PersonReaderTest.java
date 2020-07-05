package manfred.game.interact;

import manfred.game.GameConfig;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.ImageLoader;
import manfred.game.interact.gelaber.GelaberReader;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonReaderTest {
    private GelaberReader gelaberReaderMock;
    private PersonReader underTest;
    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() {
        gelaberReaderMock = mock(GelaberReader.class);
        imageLoaderMock = mock(ImageLoader.class);

        underTest = new PersonReader(gelaberReaderMock, mock(GameConfig.class), imageLoaderMock);
    }

    @Test
    void convertsName() throws InvalidInputException, IOException {
        Person result = underTest.convert("{name: testName, gelaber: []}");

        verify(imageLoaderMock).load("data\\persons\\testName.png");
        assertEquals("testName", result.getName());
    }

    @Test
    void triggersGelaberConversion() throws InvalidInputException {
        underTest.convert("{name: testName, gelaber: []}");

        verify(gelaberReaderMock).convert(any(JSONArray.class));
    }
}