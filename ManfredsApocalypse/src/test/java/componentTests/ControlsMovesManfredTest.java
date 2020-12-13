package componentTests;

import helpers.ResultCaptor;
import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.game.attack.*;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.characters.SkillSet;
import manfred.game.controls.DoNothingController;
import manfred.game.controls.GelaberController;
import manfred.game.controls.KeyControls;
import manfred.game.controls.ManfredController;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.*;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.function.Consumer;

import static helpers.AttackCombinationHelper.attackCombination;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControlsMovesManfredTest extends ControllerTestCase {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private Manfred manfred;
    private Manfred manfredSpy;
    private KeyControls controls;
    private MapWrapper mapWrapperMock;
    private SkillSet skillSetMock;
    private AttacksContainer attacksContainerMock;
    private TestGameConfig testGameConfig;
    private BackgroundScroller backgroundScrollerMock;
    private Caster attackCaster;

    @BeforeEach
    void init() {
        testGameConfig = (new TestGameConfig()).setPixelBlockSize(PIXEL_BLOCK_SIZE);

        MapCollider colliderMock = mock(MapCollider.class);
        when(colliderMock.collides(0, 0, 0, 0)).thenReturn(true);

        Map mapMock = mock(Map.class);
        when(mapMock.stepOn(anyInt(), anyInt())).thenReturn(null);
        mapWrapperMock = mock(MapWrapper.class);
        when(mapWrapperMock.getMap()).thenReturn(mapMock);

        skillSetMock = mock(SkillSet.class);
        attacksContainerMock = mock(AttacksContainer.class);

        backgroundScrollerMock = mock(BackgroundScroller.class);

        manfred = new Manfred(10, 0, 0, PIXEL_BLOCK_SIZE, PIXEL_BLOCK_SIZE, 1, colliderMock, mapWrapperMock, testGameConfig, null);
        manfredSpy = spy(manfred);

        CastModeOn castModeOn = new CastModeOn(skillSetMock, attacksContainerMock, testGameConfig, manfred.getSprite(), null);
        this.attackCaster = new Caster(new CastModeOff(castModeOn));

        setupController(manfred, attackCaster);
    }

    private void setupController(Manfred manfred, Caster attackCaster) {
        ManfredController manfredController = new ManfredController(manfred, attackCaster);
        GelaberController gelaberController = new GelaberController();
        GamePanel panel = mock(GamePanel.class);

        controls = new KeyControls(
                manfredController,
                gelaberController,
                mock(DoNothingController.class),
                manfred,
                panel,
                mapWrapperMock,
                testGameConfig,
                backgroundScrollerMock
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
        doAnswer(resultCaptor).when(mapSpy).getInteractable(any());

        controls.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));
        assertTrue(resultCaptor.getResult() instanceof Idle);
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
        setupMapWithDoorOrPortal(new Door(targetName, targetSpawnX, targetSpawnY, null));

        KeyControls controlsSpy = spy(controls);
        controlsSpy.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        Thread.sleep(1000); // Wait for swing worker to finish

        verify(controlsSpy).turnOffControls();
        verify(controlsSpy).controlManfred();
        verify(mapWrapperMock).loadMap(targetName);
        verify(backgroundScrollerMock).centerTo(manfred.getSprite().getCenter());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnX, manfred.getX());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnY, manfred.getY());
    }

    @Test
    void stepOnPortal() throws InterruptedException, InvalidInputException, IOException {
        String targetName = "target";
        int targetSpawnX = 5;
        int targetSpawnY = 66;
        setupMapWithDoorOrPortal(new Portal(targetName, targetSpawnX, targetSpawnY));

        manfred.setY(PIXEL_BLOCK_SIZE);
        Consumer<KeyControls> result = manfred.move();

        KeyControls controlsSpy = spy(controls);
        result.accept(controlsSpy);

        Thread.sleep(1000); // Wait for swing worker to finish

        verify(controlsSpy).turnOffControls();
        verify(controlsSpy).controlManfred();
        verify(mapWrapperMock).loadMap(targetName);
        verify(backgroundScrollerMock).centerTo(manfred.getSprite().getCenter());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnX, manfred.getX());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnY, manfred.getY());
    }

    @Test
    void givenCorrectCombination_thenTriggersAttack() {
        Attack attackMock = mockSkillSetWithCombination(attackCombination(CombinationElement.LEFT));

        controls.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));
        controls.keyPressed(mockEventWithKey(KeyEvent.VK_LEFT));
        controls.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));

        verify(attacksContainerMock).add(attackMock);
    }

    @Test
    void givenWrongCombination_thenDoesNotTriggerAttack() {
        Attack attackMock = mockSkillSetWithCombination(attackCombination(CombinationElement.LEFT, CombinationElement.LEFT));

        controls.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));
        controls.keyPressed(mockEventWithKey(KeyEvent.VK_LEFT));
        controls.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));

        verify(attacksContainerMock, never()).add(attackMock);
    }

    private Attack mockSkillSetWithCombination(Stack<CombinationElement> combination) {
        Attack attackMock = mock(Attack.class);
        AttackGenerator attacksGeneratorMock = mock(AttackGenerator.class);
        when(attacksGeneratorMock.generate(any(), any())).thenReturn(attackMock);

        when(skillSetMock.get(combination)).thenReturn(java.util.Optional.of(attacksGeneratorMock));
        return attackMock;
    }

    private void setupMapWithDoorOrPortal(Interactable doorOrPortal) {
        HashMap<String, Interactable> interactables = new HashMap();
        interactables.put("doorOrPortal", doorOrPortal);

        Map map = TestMapFactory.create(new String[][]{{"1", "doorOrPortal"}}, interactables);
        when(mapWrapperMock.getMap()).thenReturn(map);

        setupController(manfred, this.attackCaster);
    }

    private Person setupMapWithOpaAndManfredSpy() {
        Person opaMock = mock(Person.class);
        when(opaMock.interact()).thenCallRealMethod();

        HashMap<String, Interactable> interactables = new HashMap();
        interactables.put("Opa", opaMock);

        Map map = TestMapFactory.create(new String[][]{{"1", "Opa"}}, interactables);
        when(mapWrapperMock.getMap()).thenReturn(map);

        setupController(manfredSpy, this.attackCaster);
        return opaMock;
    }
}