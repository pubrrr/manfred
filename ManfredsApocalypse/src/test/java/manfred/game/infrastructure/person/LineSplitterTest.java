package manfred.game.infrastructure.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class LineSplitterTest {

    private LineSplitter underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new LineSplitter(5);
    }

    @Test
    void emtpySting() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("");

        assertThat(result, hasSize(1));
        assertThat(result.get(0), emptyString());
    }

    @Test
    void oneWordFittingInOneLine() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("one");

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalToObject("one"));
    }

    @Test
    void oneWordTooLongForOneLine() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("123456789");

        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalToObject("12345 "));
        assertThat(result.get(1), equalToObject("6789"));
    }

    @Test
    void twoWordsFittingInOneLine() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("a b");

        assertThat(result, hasSize(1));
        assertThat(result.get(0), equalToObject("a b"));
    }

    @Test
    void threeWordsFittingInTwoLines() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("a b next");

        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalToObject("a b "));
        assertThat(result.get(1), equalToObject("next"));
    }

    @Test
    void twoWordsWithLineBreak() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("one\ntwo");

        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalToObject("one"));
        assertThat(result.get(1), equalToObject("two"));
    }

    @Test
    void twoWordsWithTwoLineBreaks() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("one\n\ntwo");

        assertThat(result, hasSize(3));
        assertThat(result.get(0), equalToObject("one"));
        assertThat(result.get(1), emptyString());
        assertThat(result.get(2), equalToObject("two"));
    }

    @Test
    void twoWordsWithLineBreakAndWhitespacesToTrim() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("a \n b");

        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalToObject("a"));
        assertThat(result.get(1), equalToObject("b"));
    }
}