package componentTests;

import helpers.ResultCaptor;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControlsGelaberTest extends ControllerTestCase {
    private KeyControls controls;
    private GelaberController gelaberController;
    private ManfredController manfredControllerMock;

    @BeforeEach
    void init() {
        manfredControllerMock = mock(ManfredController.class);

        gelaberController = new GelaberController();

        controls = new KeyControls(manfredControllerMock, gelaberController, mock(GamePanel.class));
        controls.controlGelaber(mock(Gelaber.class));
    }

    @Test
    void nextLine_whenEnterPressed() {
        AbstractGelaberText gelaberTextSpy = setupControllerWithGelaberTextSpy(
                new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5"})
        );

        ResultCaptor<GelaberNextResponse> resultCaptor = new ResultCaptor<>();
        doAnswer(resultCaptor).when(gelaberTextSpy).next();

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(resultCaptor.getResult().continueTalking());
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        AbstractGelaberText gelaberTextSpy = setupControllerWithGelaberTextSpy(
                new GelaberText(new String[]{"line1"})
        );

        ResultCaptor<GelaberNextResponse> resultCaptor = new ResultCaptor<>();
        doAnswer(resultCaptor).when(gelaberTextSpy).next();

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertFalse(resultCaptor.getResult().continueTalking());

        controls.keyPressed(mockEventWithKey(KeyEvent.VK_S));

        verify(manfredControllerMock).keyPressed(any());
    }

    @Test
    void selectChoicesSequence() {
        SelectionMarker selectionMarkerSpy = spy(new SelectionMarker());
        HashMap choicesMock = setupChoicesMock("key", "line");
        ResultCaptor<GelaberNextResponse> resultCaptor = new ResultCaptor<>();

        AbstractGelaberText gelaberChoicesSpy = setupControllerWithGelaberTextSpy(
                new GelaberChoices(new String[]{"line1"}, choicesMock, selectionMarkerSpy)
        );

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, never()).translate(anyInt(), anyInt());

        doAnswer(resultCaptor).when(gelaberChoicesSpy).next();
        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        assertTrue(resultCaptor.getResult().continueTalking());

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, atLeastOnce()).translate(anyInt(), anyInt());

        doAnswer(resultCaptor).when(gelaberChoicesSpy).next();
        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        assertTrue(resultCaptor.getResult().continueTalking());

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(selectionMarkerSpy, atMostOnce()).translate(anyInt(), anyInt());

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        verify(manfredControllerMock, never()).keyReleased(any());
        controls.keyReleased(mockEventWithKey(KeyEvent.VK_S));
        verify(manfredControllerMock, atLeastOnce()).keyReleased(any());
    }

    private AbstractGelaberText setupControllerWithGelaberTextSpy(AbstractGelaberText gelaberText) {
        AbstractGelaberText gelaberTextSpy = spy(gelaberText);
        Gelaber gelaber = new Gelaber(new AbstractGelaberText[]{gelaberTextSpy});
        gelaberController.setGelaber(gelaber);
        return gelaberTextSpy;
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
