package componentTests;

import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.*;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Set;

import static org.mockito.Mockito.*;

public class ControlsGelaberTest extends ControllerTestCase {
    private KeyControls controlsSpy;
    private GelaberController gelaberController;
    private ManfredController manfredControllerMock;

    @BeforeEach
    void init() {
        manfredControllerMock = mock(ManfredController.class);

        gelaberController = new GelaberController();

        KeyControls controls = new KeyControls(manfredControllerMock, gelaberController, mock(GamePanel.class), mock(MapWrapper.class));
        controls.controlGelaber(mock(Gelaber.class));

        controlsSpy = spy(controls);
    }

    @Test
    void nextLine_whenEnterPressed() {
        setupControllerWithGelaberText(new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5"}));

        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, never()).controlManfred();
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        setupControllerWithGelaberText(new GelaberText(new String[]{"line1"}));

        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        verify(controlsSpy, atLeastOnce()).controlManfred();

        controlsSpy.keyPressed(mockEventWithKey(KeyEvent.VK_S));
        verify(manfredControllerMock).keyPressed(any());
    }

    @Test
    void selectChoicesSequence() {
        SelectionMarker selectionMarkerSpy = spy(new SelectionMarker());
        HashMap choicesMock = setupChoicesMock("key", "line");

        setupControllerWithGelaberText(new GelaberChoices(new String[]{"line1"}, choicesMock, selectionMarkerSpy));

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
        Gelaber gelaber = new Gelaber(new AbstractGelaberText[]{gelaberText});
        gelaberController.setGelaber(gelaber);
    }

    private HashMap setupChoicesMock(String key, String line) {
        Set<String> keySetMock = mock(Set.class);
        when(keySetMock.toArray(any())).thenReturn(new String[]{key});

        HashMap choicesMock = mock(HashMap.class);
        when(choicesMock.keySet()).thenReturn(keySetMock);
        when(choicesMock.get(key)).thenReturn(new GelaberText(new String[]{line}));
        return choicesMock;
    }
}
