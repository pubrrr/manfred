package componentTests;

import helpers.TestGameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.gelaber.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControlsGelaberTest extends ControllerTestCase {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    private GelaberController underTest;
    private ControllerInterface previous;
    private TestGameConfig testGameConfig;

    @BeforeEach
    void init() {
        previous = mock(ControllerInterface.class);
        testGameConfig = (new TestGameConfig()).withNumberOfTextLines(NUMBER_OF_TEXT_LINES);
    }

    @Test
    void nextLine_whenEnterPressed() {
        setupControllerWithGelaberText(new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5"}, testGameConfig));

        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(newControllerState instanceof GelaberController);
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        setupControllerWithGelaberText(new GelaberText(new String[]{"line1"}, testGameConfig));

        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertSame(previous, newControllerState);
    }

    @Test
    void selectChoicesSequence() {
        SelectionMarker selectionMarkerSpy = spy(new SelectionMarker(testGameConfig));
        HashMap<String, AbstractGelaberText> choicesMock = setupChoicesMock("key", "line");

        setupControllerWithGelaberText(new GelaberChoices(new String[]{"line1"}, choicesMock, selectionMarkerSpy, testGameConfig));

        assertNoSelectionYetPossible(selectionMarkerSpy);
        assertTriggerChoiceSelection(selectionMarkerSpy);
        assertSelectingChoiceDisablesSelectModeAgain(selectionMarkerSpy);
        assertEndingConversationReturnsControlsToManfred();
    }

    private void assertNoSelectionYetPossible(SelectionMarker selectionMarkerSpy) {
        underTest.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, never()).translate(anyInt(), anyInt());
    }

    private void assertTriggerChoiceSelection(SelectionMarker selectionMarkerSpy) {
        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        assertTrue(newControllerState instanceof GelaberController);

        underTest.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, atLeastOnce()).translate(anyInt(), anyInt());
    }

    private void assertSelectingChoiceDisablesSelectModeAgain(SelectionMarker selectionMarkerSpy) {
        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        assertTrue(newControllerState instanceof GelaberController);

        underTest.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, atMostOnce()).translate(anyInt(), anyInt());
    }

    private void assertEndingConversationReturnsControlsToManfred() {
        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        assertSame(previous, newControllerState);
    }

    private void setupControllerWithGelaberText(AbstractGelaberText gelaberText) {
        underTest = new GelaberController(
            this.previous,
            new Gelaber(new AbstractGelaberText[]{gelaberText}),
            mock(GelaberOverlay.class)
        );
    }

    private HashMap<String, AbstractGelaberText> setupChoicesMock(String key, String line) {
        HashMap<String, AbstractGelaberText> choices = new HashMap<>();
        choices.put(key, new GelaberText(new String[]{line}, testGameConfig));
        return choices;
    }
}
