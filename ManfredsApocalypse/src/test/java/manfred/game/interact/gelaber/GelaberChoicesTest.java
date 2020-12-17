package manfred.game.interact.gelaber;

import helpers.TestGameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class GelaberChoicesTest {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    private GelaberChoices underTest;
    private SelectionMarker selectionMarkerMock;
    private HashMap choices;

    @BeforeEach
    void setupWithNextGelaber() {
        Set<String> keySet = mock(Set.class);
        when(keySet.toArray((Object[]) any())).thenReturn(new String[]{"key"});

        choices = mock(HashMap.class);
        when(choices.keySet()).thenReturn(keySet);

        selectionMarkerMock = mock(SelectionMarker.class);

        underTest = new GelaberChoices(new String[]{"line1"}, choices, selectionMarkerMock, (new TestGameConfig()).withNumberOfTextLines(NUMBER_OF_TEXT_LINES));
    }

    @Test
    void textAndChoiceSequence_givenNoNextGelaber_upAndDownOnlyWorksAfterNoLinesRemainingAndControlsSwitchedToManfred() {
        when(choices.get("key")).thenReturn(null);

        Gelaber gelaber = new Gelaber(new AbstractGelaberText[]{mock(GelaberText.class)});

        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, never()).translate(anyInt(), anyInt());

        Function<Gelaber, Function<GelaberController, ControllerInterface>> response1 = underTest.next();

        GelaberController gelaberControllerMock = mock(GelaberController.class);
        ControllerInterface newControllerState = response1.apply(gelaber).apply(gelaberControllerMock);
        assertSame(gelaberControllerMock, newControllerState);

        underTest.up();
        underTest.down();
        verify(selectionMarkerMock, times(2)).translate(anyInt(), anyInt());

        Function<Gelaber, Function<GelaberController, ControllerInterface>> response2 = underTest.next();

        verify(selectionMarkerMock).resetToTop();
        response2.apply(gelaber).apply(gelaberControllerMock);
        verify(gelaberControllerMock).previous();
    }

    @Test
    void textAndChoiceSequence_givenNextGelaber_thenControlsNotSwitchedToManfred() {
        AbstractGelaberText gelaberTextMock = new GelaberText(new String[]{"line"}, (new TestGameConfig()).withNumberOfTextLines(NUMBER_OF_TEXT_LINES));
        when(choices.get("key")).thenReturn(gelaberTextMock);

        Gelaber gelaberMock = mock(Gelaber.class);

        Function<Gelaber, Function<GelaberController, ControllerInterface>> response1 = underTest.next();

        GelaberController gelaberControllerMock = mock(GelaberController.class);
        ControllerInterface newControllerState = response1.apply(gelaberMock).apply(gelaberControllerMock);
        assertSame(gelaberControllerMock, newControllerState);

        Function<Gelaber, Function<GelaberController, ControllerInterface>> response2 = underTest.next();

        verify(selectionMarkerMock).resetToTop();
        ControllerInterface newControllerState2 = response2.apply(gelaberMock).apply(gelaberControllerMock);
        assertSame(gelaberControllerMock, newControllerState2);
    }
}