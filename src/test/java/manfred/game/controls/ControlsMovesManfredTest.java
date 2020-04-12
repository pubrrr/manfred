package manfred.game.controls;

import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.interact.Interact;
import manfred.game.interact.Person;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControlsMovesManfredTest {
    private Manfred manfred;
    private KeyControls controls;
    private MapWrapper mapWrapperMock;

    static Interact getInteractAnswer;

    @BeforeEach
    void init() {
        MapCollider colliderMock = mock(MapCollider.class);
        when(colliderMock.collides(0, 0, 0, 0)).thenReturn(true);

        mapWrapperMock = mock(MapWrapper.class);

        manfred = new Manfred(0, 0, colliderMock, mapWrapperMock);
        controls = new KeyControls(manfred);
    }

    @Test
    void movesRightAndStops() {
        int initialX = manfred.getX();
        int initialY = manfred.getY();

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_D);

        controls.keyPressed(eventMock);
        manfred.move();

        assertTrue(initialX < manfred.getX());
        assertSame(initialY, manfred.getY());

        assertStops(eventMock, manfred.getX(), manfred.getY());
    }

    @Test
    void movesLeftAndStops() {
        int initialX = manfred.getX();
        int initialY = manfred.getY();

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_A);

        controls.keyPressed(eventMock);
        manfred.move();

        assertTrue(initialX > manfred.getX());
        assertSame(initialY, manfred.getY());

        assertStops(eventMock, manfred.getX(), manfred.getY());
    }

    @Test
    void movesUpAndStops() {
        int initialX = manfred.getX();
        int initialY = manfred.getY();

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_W);

        controls.keyPressed(eventMock);
        manfred.move();

        assertSame(initialX, manfred.getX());
        assertTrue(initialY > manfred.getY());

        assertStops(eventMock, manfred.getX(), manfred.getY());
    }

    @Test
    void movesDownAndStops() {
        int initialX = manfred.getX();
        int initialY = manfred.getY();

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_S);

        controls.keyPressed(eventMock);
        manfred.move();

        assertSame(initialX, manfred.getX());
        assertTrue(initialY < manfred.getY());

        assertStops(eventMock, manfred.getX(), manfred.getY());
    }

    private void assertStops(KeyEvent eventMock, int afterMoveX, int afterMoveY) {
        controls.keyReleased(eventMock);
        manfred.move();

        assertSame(afterMoveX, manfred.getX());
        assertSame(afterMoveY, manfred.getY());
    }

    private KeyEvent mockEventWithKey(int keyPressed) {
        KeyEvent eventMock = mock(KeyEvent.class);
        when(eventMock.getKeyCode()).thenReturn(keyPressed);
        return eventMock;
    }

    @Test
    void interactDoesNothingWhenNoInteractInReach() {
        Map map = new Map("test", new String[][]{{"1", "1"}}, null);
        Map mapSpy = spy(map);
        when(mapWrapperMock.getMap()).thenReturn(mapSpy);

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_ENTER);
        doAnswer(invocationOnMock -> {
            Object result = invocationOnMock.callRealMethod();
            ControlsMovesManfredTest.getInteractAnswer = (Interact) result;
            return result;
        }).when(mapSpy).getInteract(anyInt(), anyInt());

        controls.keyPressed(eventMock);

        assertNull(getInteractAnswer);
    }

    @Test
    void interactWithInteract() {
        Person opaMock = mock(Person.class);
        HashMap interactsMock = mock(HashMap.class);
        when(interactsMock.get("Opa")).thenReturn(opaMock);

        Map map = new Map("test", new String[][]{{"1", "Opa"}}, interactsMock);
        when(mapWrapperMock.getMap()).thenReturn(map);

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_ENTER);
        controls.keyPressed(eventMock);

        verify(opaMock).interact();
    }
}