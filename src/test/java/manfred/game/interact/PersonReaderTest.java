package manfred.game.interact;

import manfred.game.exception.InvalidInputException;
import manfred.game.interact.gelaber.GelaberReader;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonReaderTest {
    private GelaberReader gelaberReaderMock;
    private PersonReader underTest;

    @BeforeEach
    void init() {
        gelaberReaderMock = mock(GelaberReader.class);

        underTest = new PersonReader(gelaberReaderMock);
    }

    @Test
    void convertsName() throws InvalidInputException {
        Person result = underTest.convert("{name: testName, gelaber: []}");

        assertEquals("testName", result.getName());
    }

    @Test
    void triggersGelaberConversion() throws InvalidInputException {
        underTest.convert("{name: testName, gelaber: []}");

        verify(gelaberReaderMock).convert(any(JSONArray.class));
    }
}