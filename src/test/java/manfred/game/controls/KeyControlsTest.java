package manfred.game.controls;

import manfred.game.Manfred;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class KeyControlsTest {
    private static KeyControls underTest;
    private static Manfred manfredMock;

    @BeforeAll
    static void init() {
        manfredMock = mock(Manfred.class);
        underTest = new KeyControls(manfredMock);
    }

    @Test
    void keyPressedNotifiesManfred() {
        KeyEvent eventMock = mock(KeyEvent.class);

        underTest.keyPressed(eventMock);
        verify(manfredMock).keyPressed(eventMock);
    }

    @Test
    void keyReleasedNotifiesManfred() {
        KeyEvent eventMock = mock(KeyEvent.class);

        underTest.keyReleased(eventMock);
        verify(manfredMock).keyReleased(eventMock);
    }
}