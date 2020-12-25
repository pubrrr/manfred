package manfred.game.interact.person;

import helpers.TestGameConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.GelaberEdgeHelper.edge;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;

class ChoicesTextLineFactoryTest {

    private ChoicesTextLineFactory underTest;

    @BeforeEach
    void setUp() {
        this.underTest = new ChoicesTextLineFactory((new TestGameConfig()).withNumberOfTextLines(2));
    }

    @Test
    void givenOneLine_thenDoesNotWrap() {
        TextLine result = underTest.create(new GelaberNode(List.of("one line")), List.of(edge("id")));

        assertThat(result, instanceOf(ChoicesText.class));
    }

    @Test
    void givenMoreLinesThanFitInBox_thenWraps() {
        TextLine result = underTest.create(new GelaberNode(List.of("1st line", "2nd line", "3rd line")), List.of(edge("id")));

        assertThat(result, instanceOf(TextLineWrapper.class));
        assertThat(result.next(identifier -> mock(TextLine.class)).getNextTextLine(), instanceOf(ChoicesText.class));
    }
}