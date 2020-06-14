package manfred.game.interact.gelaber;

import helpers.TestGameConfig;
import manfred.game.controls.KeyControls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class GelaberChoicesTest {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    private GelaberChoices underTest;
    private SelectionMarker selectionMarkerMock;
    private HashMap choices;

    @BeforeEach
    void setupWithNextGelaber() {
        Set<String> keySet = mock(Set.class);
        when(keySet.toArray(any())).thenReturn(new String[]{"key"});

        choices = mock(HashMap.class);
        when(choices.keySet()).thenReturn(keySet);

        selectionMarkerMock = mock(SelectionMarker.class);

        underTest = new GelaberChoices(new String[]{"line1"}, choices, selectionMarkerMock, (new TestGameConfig()).setNumberOfTextLines(NUMBER_OF_TEXT_LINES));
    }

    @Test
    void textAndChoiceSequence_givenNoNextGelaber_upAndDownOnlyWorksAfterNoLinesRemainingAndControlsSwitchedToManfred() {
        when(choices.get("key")).thenReturn(null);

        Gelaber gelaberMock = mock(Gelaber.class);

        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, never()).translate(anyInt(), anyInt());

        Function<Gelaber, Consumer<KeyControls>> response1 = underTest.next();
        assertNull(response1.apply(gelaberMock));

        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, times(2)).translate(anyInt(), anyInt());

        Function<Gelaber, Consumer<KeyControls>> response2 = underTest.next();

        verify(selectionMarkerMock).resetToTop();
        response2.apply(gelaberMock);
        verify(gelaberMock).switchControlsBackToManfred();
    }

    @Test
    void textAndChoiceSequence_givenNextGelaber_thenControlsNotSwitchedToManfred() {
        AbstractGelaberText gelaberTextMock = new GelaberText(new String[]{"line"}, (new TestGameConfig()).setNumberOfTextLines(NUMBER_OF_TEXT_LINES));
        when(choices.get("key")).thenReturn(gelaberTextMock);

        Gelaber gelaberMock = mock(Gelaber.class);

        Function<Gelaber, Consumer<KeyControls>> response1 = underTest.next();
        assertNull(response1.apply(gelaberMock));

        Function<Gelaber, Consumer<KeyControls>> response2 = underTest.next();

        verify(selectionMarkerMock).resetToTop();
        response2.apply(gelaberMock);
        verify(gelaberMock).setCurrentText(gelaberTextMock);
    }
}