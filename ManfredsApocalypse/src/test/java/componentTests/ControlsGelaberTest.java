package componentTests;

import helpers.TestGameConfig;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ControlsGelaberTest extends ControllerTestCase {
    private static final int NUMBER_OF_TEXT_LINES = 5;

    private KeyControls controlsSpy;
    private GelaberController gelaberController;
    private ManfredController manfredControllerMock;
    private TestGameConfig testGameConfig;

    @BeforeEach
    void init() {
        manfredControllerMock = mock(ManfredController.class);
//        when(manfredControllerMock.keyReleased(any())).thenReturn(KeyControls::doNothing);

//        gelaberController = new GelaberController();
        testGameConfig = (new TestGameConfig()).setNumberOfTextLines(NUMBER_OF_TEXT_LINES);

        KeyControls controls = new KeyControls(gelaberController, mock(GamePanel.class));
        controls.controlGelaber(mock(Gelaber.class));

        controlsSpy = spy(controls);
    }

    @Test
    void nextLine_whenEnterPressed() {
        setupControllerWithGelaberText(new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5"}, testGameConfig));

        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, never()).controlManfred();
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        setupControllerWithGelaberText(new GelaberText(new String[]{"line1"}, testGameConfig));

        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, atLeastOnce()).controlManfred();

        controlsSpy.keyPressed(mockEventWithKey(KeyEvent.VK_S));
        verify(manfredControllerMock).keyPressed(any());
    }

    @Test
    void selectChoicesSequence() {
        SelectionMarker selectionMarkerSpy = spy(new SelectionMarker(testGameConfig));
        HashMap choicesMock = setupChoicesMock("key", "line");

        setupControllerWithGelaberText(new GelaberChoices(new String[]{"line1"}, choicesMock, selectionMarkerSpy, testGameConfig));

        assertNoSelectionYetPossible(selectionMarkerSpy);
        assertTriggerChoiceSelection(selectionMarkerSpy);
        assertSelectingChoiceDisablesSelectModeAgain(selectionMarkerSpy);
        assertEndingConversationReturnsControlsToManfred();
    }

    private void assertNoSelectionYetPossible(SelectionMarker selectionMarkerSpy) {
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, never()).translate(anyInt(), anyInt());
    }

    private void assertTriggerChoiceSelection(SelectionMarker selectionMarkerSpy) {
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, never()).controlManfred();

        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, atLeastOnce()).translate(anyInt(), anyInt());
    }

    private void assertSelectingChoiceDisablesSelectModeAgain(SelectionMarker selectionMarkerSpy) {
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, never()).controlManfred();

        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, atMostOnce()).translate(anyInt(), anyInt());
    }

    private void assertEndingConversationReturnsControlsToManfred() {
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, atLeastOnce()).controlManfred();

        verify(manfredControllerMock, never()).keyReleased(any());
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(manfredControllerMock, atLeastOnce()).keyReleased(any());
    }

    private void setupControllerWithGelaberText(AbstractGelaberText gelaberText) {
        Gelaber gelaber = new Gelaber(new AbstractGelaberText[]{gelaberText}, testGameConfig);
        gelaberController.setGelaber(gelaber);
    }

    private HashMap setupChoicesMock(String key, String line) {
        Set<String> keySetMock = mock(Set.class);
        when(keySetMock.toArray((Object[]) any())).thenReturn(new String[]{key});

        HashMap choicesMock = mock(HashMap.class);
        when(choicesMock.keySet()).thenReturn(keySetMock);
        when(choicesMock.get(key)).thenReturn(new GelaberText(new String[]{line}, testGameConfig));
        return choicesMock;
    }
}
