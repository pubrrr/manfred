package componentTests;

import java.awt.event.KeyEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControllerTestCase {
    protected KeyEvent mockEventWithKey(int keyPressed) {
        KeyEvent eventMock = mock(KeyEvent.class);
        when(eventMock.getKeyCode()).thenReturn(keyPressed);
        return eventMock;
    }
}
