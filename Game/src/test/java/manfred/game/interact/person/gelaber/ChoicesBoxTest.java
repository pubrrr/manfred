package manfred.game.interact.person.gelaber;

import manfred.game.interact.person.gelaber.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChoicesBoxTest {

    private ChoicesBox underTest;
    private ChoicesFacade choicesFacadeMock;

    @BeforeEach
    void setUp() {
        choicesFacadeMock = mock(ChoicesFacade.class);
        underTest = new ChoicesBox(mock(ChoicesText.class), choicesFacadeMock);
    }

    @Test
    void next() {
        GelaberEdge edgeMock = mock(GelaberEdge.class);
        when(choicesFacadeMock.confirm()).thenReturn(edgeMock);

        TextLine textLineMock = mock(TextLine.class);
        GelaberResponseWrapper result = underTest.next(identifier -> textLineMock);

        assertSame(textLineMock, result.getNextTextLine());
    }
}