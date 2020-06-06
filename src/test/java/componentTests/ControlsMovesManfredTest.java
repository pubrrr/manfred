package componentTests;

import helpers.ResultCaptor;
import helpers.TestMapFactory;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.controls.DoNothingController;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.Door;
import manfred.game.interact.Interactable;
import manfred.game.interact.Person;
import manfred.game.interact.Portal;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControlsMovesManfredTest extends ControllerTestCase {
    private Manfred manfred;
    private Manfred manfredSpy;
    private KeyControls controls;
    private MapWrapper mapWrapperMock;

    @BeforeEach
    void init() {
        MapCollider colliderMock = mock(MapCollider.class);
        when(colliderMock.collides(0, 0, 0, 0)).thenReturn(true);

        Map mapMock = mock(Map.class);
        when(mapMock.stepOn(anyInt(), anyInt())).thenReturn(null);
        mapWrapperMock = mock(MapWrapper.class);
        when(mapWrapperMock.getMap()).thenReturn(mapMock);

        manfred = new Manfred(10, 0, 0, 1, colliderMock, mapWrapperMock);
        manfredSpy = spy(manfred);

        setupControllerWithManfred(manfred);
    }

    private void setupControllerWithManfred(Manfred manfred) {
        ManfredController manfredController = new ManfredController(manfred);
        GelaberController gelaberController = new GelaberController();
        GamePanel panel = mock(GamePanel.class);

        controls = new KeyControls(
                manfredController,
                gelaberController,
                mock(DoNothingController.class),
                manfred,
                panel,
                mapWrapperMock
        );
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
        Map map = TestMapFactory.create(new String[][]{{"1", "1"}}, null);
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

        controls.keyPressed(mockEventWithKey(KeyEvent.VK_W));
        verify(manfredSpy, never()).up();
    }

    @Test
    void interactWithDoor() throws InvalidInputException, IOException, InterruptedException {
        String targetName = "target";
        int targetSpawnX = 5;
        int targetSpawnY = 66;
        setupMapWithDoorOrPortal(new Door(targetName, targetSpawnX, targetSpawnY));

        KeyControls controlsSpy = spy(controls);
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        Thread.sleep(1000); // Wait for swing worker to finish

        verify(controlsSpy).turnOffControls();
        verify(controlsSpy).controlManfred();
        verify(mapWrapperMock).loadMap(targetName);
        assertEquals(GamePanel.PIXEL_BLOCK_SIZE * targetSpawnX, manfred.getX());
        assertEquals(GamePanel.PIXEL_BLOCK_SIZE * targetSpawnY, manfred.getY());
    }

    @Test
    void stepOnPortal() throws InterruptedException, InvalidInputException, IOException {
        String targetName = "target";
        int targetSpawnX = 5;
        int targetSpawnY = 66;
        setupMapWithDoorOrPortal(new Portal(targetName, targetSpawnX, targetSpawnY));

        manfred.setY(GamePanel.PIXEL_BLOCK_SIZE);
        Consumer<KeyControls> result = manfred.move();

        KeyControls controlsSpy = spy(controls);
        result.accept(controlsSpy);

        Thread.sleep(1000); // Wait for swing worker to finish

        verify(controlsSpy).turnOffControls();
        verify(controlsSpy).controlManfred();
        verify(mapWrapperMock).loadMap(targetName);
        assertEquals(GamePanel.PIXEL_BLOCK_SIZE * targetSpawnX, manfred.getX());
        assertEquals(GamePanel.PIXEL_BLOCK_SIZE * targetSpawnY, manfred.getY());
    }

    private void setupMapWithDoorOrPortal(Interactable doorOrPortal) {
        HashMap<String, Interactable> interactables = new HashMap();
        interactables.put("doorOrPortal", doorOrPortal);

        Map map = TestMapFactory.create(new String[][]{{"1", "doorOrPortal"}}, interactables);
        when(mapWrapperMock.getMap()).thenReturn(map);

        setupControllerWithManfred(manfred);
    }

    private Person setupMapWithOpaAndManfredSpy() {
        Person opaMock = mock(Person.class);
        when(opaMock.interact()).thenCallRealMethod();

        HashMap<String, Interactable> interactables = new HashMap();
        interactables.put("Opa", opaMock);

        Map map = TestMapFactory.create(new String[][]{{"1", "Opa"}}, interactables);
        when(mapWrapperMock.getMap()).thenReturn(map);

        setupControllerWithManfred(manfredSpy);
        return opaMock;
    }
}