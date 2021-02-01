package manfred.game.interact.person.textLineFactory;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.game.interact.person.gelaber.GelaberNode;
import manfred.game.interact.person.gelaber.SimpleTextLine;
import manfred.game.interact.person.gelaber.TextLine;
import manfred.game.interact.person.gelaber.TextLineWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static helpers.GelaberEdgeHelper.edge;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SimpleTextLineFactoryTest {

    private FactoryRule underTest;

    @BeforeEach
    void setUp() throws InvalidInputException {
        this.underTest = SimpleTextLineFactory.withConfig((new TestGameConfig()).withNumberOfTextLines(2));
    }

    @Test
    void givenOneLine_thenDoesNotWrap() {
        Optional<FactoryAction> resultingAction = underTest.applicableTo(List.of(edge("id")));

        assertFalse(resultingAction.isEmpty());

        TextLine result = resultingAction.get().create(new GelaberNode(List.of("one line")), List.of(edge("id")));

        assertThat(result, instanceOf(SimpleTextLine.class));
    }

    @Test
    void givenMoreLinesThanFitInBox_thenWraps() {
        Optional<FactoryAction> resultingAction = underTest.applicableTo(List.of(edge("id")));

        assertFalse(resultingAction.isEmpty());

        TextLine result = resultingAction.get().create(new GelaberNode(List.of("1st line", "2nd line", "3rd line")), List.of(edge("id")));

        assertThat(result, instanceOf(TextLineWrapper.class));
        assertThat(result.next(identifier -> mock(TextLine.class)).getNextTextLine(), instanceOf(SimpleTextLine.class));
    }

    @Test
    void notApplicableToMultipleOutgoingEdges() {
        Optional<FactoryAction> resultingAction = underTest.applicableTo(List.of(edge("1"), edge("2")));

        assertTrue(resultingAction.isEmpty());
    }
}