package componentTests;

import helpers.ResultCaptor;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.gelaber.AbstractGelaberText;
import manfred.game.interact.gelaber.Gelaber;
import manfred.game.interact.gelaber.GelaberText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

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

        gelaberController = new GelaberController(mock(GamePanel.class));

        controls = new KeyControls(manfredControllerMock, gelaberController, mock(GamePanel.class));
        controls.controlGelaber(mock(Gelaber.class));
        gelaberController.setKeyControls(controls);
    }

    @Test
    void nextLine_whenEnterPressed() {
        AbstractGelaberText gelaberTextSpy = setupControllerWithGelaberTextSpy(
                new GelaberText(new String[]{"line1", "line2", "line3", "line4", "line5"})
        );

        ResultCaptor<Boolean> resultCaptor = new ResultCaptor<>();
        doAnswer(resultCaptor).when(gelaberTextSpy).next();

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(resultCaptor.getResult());
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        AbstractGelaberText gelaberTextSpy = setupControllerWithGelaberTextSpy(
                new GelaberText(new String[]{"line1"})
        );

        ResultCaptor<Boolean> resultCaptor = new ResultCaptor<>();
        doAnswer(resultCaptor).when(gelaberTextSpy).next();

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertFalse(resultCaptor.getResult());

        controls.keyPressed(mockEventWithKey(KeyEvent.VK_S));

        verify(manfredControllerMock).keyPressed(any());
    }

    // TODO: test for choices: press enter once more. Accepts up and down.

    private AbstractGelaberText setupControllerWithGelaberTextSpy(AbstractGelaberText gelaberText) {
        AbstractGelaberText gelaberTextSpy = spy(gelaberText);
        Gelaber gelaber = new Gelaber(new AbstractGelaberText[]{gelaberTextSpy});
        gelaberController.setGelaber(gelaber);
        return gelaberTextSpy;
    }
}
