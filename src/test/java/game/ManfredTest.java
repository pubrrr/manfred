package game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ManfredTest {
    private static Manfred underTest;

    @BeforeAll
    static void init() {
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
    void movesRight() {
        int initialX = underTest.getX();
        int initialY = underTest.getY();

        KeyEvent eventMock = mock(KeyEvent.class);
        underTest.keyPressed(eventMock);
        underTest.move();

        assertTrue(initialX < underTest.getX());
        assertSame(initialY, underTest.getY());
    }
}