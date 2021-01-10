package manfred.game.interact.person.gelaber;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

class LineSplitterTest {

    private LineSplitter underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new LineSplitter(5);
    }

    @Test
    void emtpySting() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("");

        assertThat(result, Matchers.hasSize(1));
        assertThat(result.get(0), Matchers.emptyString());
    }

    @Test
    void oneWordFittingInOneLine() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("one");

        assertThat(result, Matchers.hasSize(1));
        assertThat(result.get(0), Matchers.equalToObject("one"));
    }

    @Test
    void oneWordTooLongForOneLine() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("123456789");

        assertThat(result, Matchers.hasSize(2));
        assertThat(result.get(0), Matchers.equalToObject("12345 "));
        assertThat(result.get(1), Matchers.equalToObject("6789"));
    }

    @Test
    void twoWordsFittingInOneLine() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("a b");

        assertThat(result, Matchers.hasSize(1));
        assertThat(result.get(0), Matchers.equalToObject("a b"));
    }

    @Test
    void threeWordsFittingInTwoLines() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("a b next");

        assertThat(result, Matchers.hasSize(2));
        assertThat(result.get(0), Matchers.equalToObject("a b "));
        assertThat(result.get(1), Matchers.equalToObject("next"));
    }

    @Test
    void twoWordsWithLineBreak() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("one\ntwo");

        assertThat(result, Matchers.hasSize(2));
        assertThat(result.get(0), Matchers.equalToObject("one"));
        assertThat(result.get(1), Matchers.equalToObject("two"));
    }

    @Test
    void twoWordsWithTwoLineBreaks() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("one\n\ntwo");

        assertThat(result, Matchers.hasSize(3));
        assertThat(result.get(0), Matchers.equalToObject("one"));
        assertThat(result.get(1), Matchers.emptyString());
        assertThat(result.get(2), Matchers.equalToObject("two"));
    }

    @Test
    void twoWordsWithLineBreakAndWhitespacesToTrim() {
        List<String> result = underTest.splitIntoTextLinesFittingIntoTextBox("a \n b");

        assertThat(result, Matchers.hasSize(2));
        assertThat(result.get(0), Matchers.equalToObject("a"));
        assertThat(result.get(1), Matchers.equalToObject("b"));
    }
}