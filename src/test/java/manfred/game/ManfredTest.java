package manfred.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManfredTest {
    private Manfred underTest;

    @BeforeEach
    void init() {
        underTest = new Manfred(0, 0);
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move();

        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @Test
    void movesRightAndStops() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        KeyEvent eventMock = mock(KeyEvent.class);
        when(eventMock.getKeyCode()).thenReturn(KeyEvent.VK_D);

        underTest.keyPressed(eventMock);
        underTest.move();

        assertTrue(initialX < underTest.getX());
        assertSame(initialY, underTest.getY());

        assertStops(eventMock, underTest.getX(), underTest.getY());
    }

    @Test
    void movesLeftAndStops() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        KeyEvent eventMock = mock(KeyEvent.class);
        when(eventMock.getKeyCode()).thenReturn(KeyEvent.VK_A);

        underTest.keyPressed(eventMock);
        underTest.move();

        assertTrue(initialX > underTest.getX());
        assertSame(initialY, underTest.getY());

        assertStops(eventMock, underTest.getX(), underTest.getY());
    }

    @Test
    void movesUpAndStops() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        KeyEvent eventMock = mock(KeyEvent.class);
        when(eventMock.getKeyCode()).thenReturn(KeyEvent.VK_W);

        underTest.keyPressed(eventMock);
        underTest.move();

        assertSame(initialX, underTest.getX());
        assertTrue(initialY > underTest.getY());

        assertStops(eventMock, underTest.getX(), underTest.getY());
    }

    @Test
    void movesDownAndStops() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        KeyEvent eventMock = mock(KeyEvent.class);
        when(eventMock.getKeyCode()).thenReturn(KeyEvent.VK_S);

        underTest.keyPressed(eventMock);
        underTest.move();

        assertSame(initialX, underTest.getX());
        assertTrue(initialY < underTest.getY());

        assertStops(eventMock, underTest.getX(), underTest.getY());
    }

    private void assertStops(KeyEvent eventMock, int afterMoveX, int afterMoveY) {
        underTest.keyReleased(eventMock);
        underTest.move();

        assertSame(afterMoveX, underTest.getX());
        assertSame(afterMoveY, underTest.getY());
    }
}