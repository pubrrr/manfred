package manfred.game.interact.gelaber;

import manfred.game.exception.InvalidInputException;
import org.json.JSONArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GelaberReaderTest {
    private static GelaberReader underTest;

    @BeforeAll
    static void init() {
        underTest = new GelaberReader();
    }

    @Test
    void convertEmptyArray() throws InvalidInputException {
        Gelaber result = underTest.convert(new JSONArray("[]"));

        assertEquals(0, result.getTextLines().length);
    }

    @Test
    void throwsExceptionWhenElementsAreNoJsonObjects() {
        JSONArray invalidJson = new JSONArray("[1]");

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }

    @Test
    void convertsText() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: testText, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTextLines().length);
        assertEquals("testText", result.getTextLines()[0].getText());
    }

    @Test
    void convertsType() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: testGelaber, type: gelaber}, {text: testChoicesText, type: choices}]");

        Gelaber result = underTest.convert(input);

        assertEquals(GelaberType.values().length, result.getTextLines().length);
        assertEquals(GelaberType.gelaber, result.getTextLines()[0].getType());
        assertEquals(GelaberType.choices, result.getTextLines()[1].getType());
    }

    @Test
    void invalidTypeThrowsException() {
        JSONArray invalidJson = new JSONArray("[{text: testGelaber, type: invalid}]");

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }
}