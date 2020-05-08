package manfred.game.interact.gelaber;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GelaberChoicesTest {
    private GelaberChoices underTest;
    private SelectionMarker selectionMarkerMock;

    @BeforeEach
    void init() {
        Set<String> keySet = mock(Set.class);
        when(keySet.toArray(any())).thenReturn(new String[]{"choiceskey"});

        HashMap<String, AbstractGelaberText> choices = mock(HashMap.class);
        when(choices.keySet()).thenReturn(keySet);
        when(choices.get("choicesKey")).thenReturn(null);

        selectionMarkerMock = mock(SelectionMarker.class);

        underTest = new GelaberChoices(new String[]{"line1"}, choices, selectionMarkerMock);
    }

    @Test
    void textAndChoiceSequence_upAndDownOnlyWorksAfterNoLinesRemaining() {
        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, never()).translate(anyInt(), anyInt());

        assertTrue(underTest.next().continueTalking());

        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, times(2)).translate(anyInt(), anyInt());

        GelaberNextResponse reponse = underTest.next();
        assertFalse(reponse.continueTalking());
        assertNull(reponse.getNextGelaber());
        verify(selectionMarkerMock).resetToTop();
    }
}