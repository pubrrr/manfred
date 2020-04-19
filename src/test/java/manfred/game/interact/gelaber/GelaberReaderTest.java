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
        underTest = new GelaberReader(5);
    }

    @Test
    void throwsExceptionWhenElementsAreNoJsonObjects() {
        JSONArray invalidJson = new JSONArray("[1]");

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }

    @Test
    void convertsType() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: testGelaber, type: gelaber}, {text: testChoicesText, type: choices, choices: {possibleAnswer: {text: testGelaber, type: gelaber}}}]");

        Gelaber result = underTest.convert(input);

        assertEquals(GelaberType.values().length, result.getTexts().length);
        assertTrue(result.getTexts()[0] instanceof GelaberText);
        assertTrue(result.getTexts()[1] instanceof GelaberChoices);
    }

    @Test
    void invalidTypeThrowsException() {
        JSONArray invalidJson = new JSONArray("[{text: testGelaber, type: invalid}]");

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }

    @Test
    void convertsTextFittingIntoSingleLine() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: test, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(1, result.getTexts()[0].getLines().length);
        assertEquals("test", result.getTexts()[0].getLines()[0]);
    }

    @Test
    void convertsEmptyText() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: \"\", type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(1, result.getTexts()[0].getLines().length);
        assertEquals("", result.getTexts()[0].getLines()[0]);
    }

    @Test
    void convertsTwoWordsInSingleLine() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: a a, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(1, result.getTexts()[0].getLines().length);
        assertEquals("a a", result.getTexts()[0].getLines()[0]);
    }

    @Test
    void convertsTwoWordsInTwoLines() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: two words, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(2, result.getTexts()[0].getLines().length);
        assertEquals("two ", result.getTexts()[0].getLines()[0]);
        assertEquals("words", result.getTexts()[0].getLines()[1]);
    }

    @Test
    void convertsTwoWordsInOneLine_andThirdWordInSecondLine() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: a a more, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(2, result.getTexts()[0].getLines().length);
        assertEquals("a a ", result.getTexts()[0].getLines()[0]);
        assertEquals("more", result.getTexts()[0].getLines()[1]);
    }

    @Test
    void convertsWordThatIsTooLong() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: 12345_6, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(2, result.getTexts()[0].getLines().length);
        assertEquals("12345 ", result.getTexts()[0].getLines()[0]);
        assertEquals("_6", result.getTexts()[0].getLines()[1]);
    }

    @Test
    void convertsWordThatIsTooLong_andAppendsRest() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: a b 12345_6 c, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(3, result.getTexts()[0].getLines().length);
        assertEquals("a b ", result.getTexts()[0].getLines()[0]);
        assertEquals("12345 ", result.getTexts()[0].getLines()[1]);
        assertEquals("_6 c", result.getTexts()[0].getLines()[2]);
    }

    @Test
    void convertsWordThatIsTooLong_andIsConvertedInto3Lines() throws InvalidInputException {
        JSONArray input = new JSONArray("[{text: 12345_2345_23, type: gelaber}]");

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(3, result.getTexts()[0].getLines().length);
        assertEquals("12345 ", result.getTexts()[0].getLines()[0]);
        assertEquals("_2345 ", result.getTexts()[0].getLines()[1]);
        assertEquals("_23", result.getTexts()[0].getLines()[2]);
    }

    @Test
    void convertsChoicesWithPossibleAnswers() throws InvalidInputException {
        JSONArray input = new JSONArray(
                "[{text: text, type: choices, choices: {" +
                        "answer1: {text: test, type: gelaber}," +
                        "answer2: {text: test, type: choices, choices: {nestedAnswer: {text: test, type: gelaber}}}" +
                        "}}]"
        );

        Gelaber result = underTest.convert(input);

        assertEquals(1, result.getTexts().length);
        assertEquals(2, ((GelaberChoices) result.getTexts()[0]).getChoices().size());
        assertTrue(((GelaberChoices) result.getTexts()[0]).getChoices().get("answer1") instanceof GelaberText);
        assertTrue(((GelaberChoices) result.getTexts()[0]).getChoices().get("answer2") instanceof GelaberChoices);
    }
}