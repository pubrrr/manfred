package componentTests.controller;

import helpers.TestGameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.person.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GelaberControllerComponentTest extends ControllerTestCase {
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
        setupControllerWithGelaberText(SimpleTextLine.continuing(new String[]{"text"}, null, testGameConfig));

        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(newControllerState instanceof GelaberController);
    }

    @Test
    void upAndDown() {
        setupControllerWithGelaberText(SimpleTextLine.continuing(new String[]{"text"}, null, testGameConfig));

        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_UP)) instanceof GelaberController);
        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_W)) instanceof GelaberController);

        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_DOWN)) instanceof GelaberController);
        assertTrue(underTest.keyReleased(mockEventWithKey(KeyEvent.VK_S)) instanceof GelaberController);
    }

    @Test
    void returnsControlsToManfred_whenEnterPressedAndNoFurtherText() {
        setupControllerWithGelaberText(SimpleTextLine.aborting(new String[]{"text"}, null, testGameConfig));

        ControllerInterface newControllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertSame(previous, newControllerState);
    }

    private void setupControllerWithGelaberText(TextLine textLine) {
        underTest = new GelaberController(
            this.previous,
            new GelaberFacade(textLine),
            mock(GelaberOverlay.class)
        );
    }
}
