package manfred.game.interact.gelaber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GelaberChoicesTest {
    private GelaberChoices underTest;
    private Polygon selectionMarkerMock;

    @BeforeEach
    void init() {
        HashMap<String, AbstractGelaberText> choices = mock(HashMap.class);
        selectionMarkerMock = mock(Polygon.class);

        underTest = new GelaberChoices(new String[]{"line1"}, choices, selectionMarkerMock);
    }

    @Test
    void textAndChoiceSequence_upAndDownOnlyWorksAfterNoLinesRemaining() {
        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, never()).translate(anyInt(), anyInt());

        assertTrue(underTest.next());

        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, times(2)).translate(anyInt(), anyInt());

        assertFalse(underTest.next());
    }
}