package manfred.game.interact.person.textLineFactory;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.game.interact.person.gelaber.ChoicesText;
import manfred.game.interact.person.gelaber.GelaberNode;
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

class ChoicesTextLineFactoryTest {

    private FactoryRule underTest;

    @BeforeEach
    void setUp() {
        this.underTest = ChoicesTextLineFactory.withConfig((new TestGameConfig()).withNumberOfTextLines(2));
    }

    @Test
    void givenOneLine_thenDoesNotWrap() {
        Optional<FactoryAction> resultingAction = underTest.applicableTo(List.of(edge("1"), edge("2")));

        assertFalse(resultingAction.isEmpty());

        TextLine result = resultingAction.get().create(new GelaberNode(List.of("one line")), List.of(edge("1"), edge("2")));


        assertThat(result, instanceOf(ChoicesText.class));
    }

    @Test
    void givenMoreLinesThanFitInBox_thenWraps() {
        Optional<FactoryAction> resultingAction = underTest.applicableTo(List.of(edge("1"), edge("2")));

        assertFalse(resultingAction.isEmpty());

        TextLine result = resultingAction.get().create(new GelaberNode(List.of("1st line", "2nd line", "3rd line")), List.of(edge("1"), edge("2")));

        assertThat(result, instanceOf(TextLineWrapper.class));
        assertThat(result.next(identifier -> mock(TextLine.class)).getNextTextLine(), instanceOf(ChoicesText.class));
    }

    @Test
    void notApplicableToOneOutgoingEdge() {
        Optional<FactoryAction> resultingAction = underTest.applicableTo(List.of(edge("1")));

        assertTrue(resultingAction.isEmpty());
    }
}