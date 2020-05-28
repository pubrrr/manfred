package componentTests;

import helpers.ResultCaptor;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.Interactable;
import manfred.game.interact.Person;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControlsMovesManfredTest extends ControllerTestCase{
    private Manfred manfred;
    private Manfred manfredSpy;
    private KeyControls controls;
    private MapWrapper mapWrapperMock;

    @BeforeEach
    void init() {
        MapCollider colliderMock = mock(MapCollider.class);
        when(colliderMock.collides(0, 0, 0, 0)).thenReturn(true);

        mapWrapperMock = mock(MapWrapper.class);

        manfred = new Manfred(0, 0, colliderMock, mapWrapperMock);
        manfredSpy = spy(manfred);

        setupControllerWithManfred(manfred);
    }

    private void setupControllerWithManfred(Manfred manfred) {
        ManfredController manfredController = new ManfredController(manfred);
        GelaberController gelaberController = new GelaberController();
        GamePanel panel = mock(GamePanel.class);

        controls = new KeyControls(manfredController, gelaberController, mock(Manfred.class), panel, mapWrapperMock);
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

    @Test
    void interactDoesNothingWhenNoInteractInReach() {
        Map map = new Map("test", new String[][]{{"1", "1"}}, null);
        Map mapSpy = spy(map);
        when(mapWrapperMock.getMap()).thenReturn(mapSpy);

        ResultCaptor<Interactable> resultCaptor = new ResultCaptor<>();
        doAnswer(resultCaptor).when(mapSpy).getInteractable(anyInt(), anyInt());

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertNull(resultCaptor.getResult());
    }

    @Test
    void interactReturnsInteract_andTurnsOffManfredControls() {
        Person opaMock = setupMapWithOpaAndManfredSpy();

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        verify(opaMock, atLeastOnce()).interact();

        KeyEvent eventMock2 = mockEventWithKey(KeyEvent.VK_W);
        controls.keyPressed(eventMock2);

        verify(manfredSpy, never()).up();
    }

    private Person setupMapWithOpaAndManfredSpy() {
        Person opaMock = mock(Person.class);
        when(opaMock.interact()).thenCallRealMethod();

        HashMap interactablesMock = mock(HashMap.class);
        when(interactablesMock.get("Opa")).thenReturn(opaMock);

        Map map = new Map("test", new String[][]{{"1", "Opa"}}, interactablesMock);
        when(mapWrapperMock.getMap()).thenReturn(map);

        setupControllerWithManfred(manfredSpy);
        return opaMock;
    }
}