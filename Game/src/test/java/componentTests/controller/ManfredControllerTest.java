package componentTests.controller;

import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.data.InvalidInputException;
import manfred.game.attack.*;
import manfred.game.characters.Manfred;
import manfred.game.characters.MapCollider;
import manfred.game.characters.SkillSet;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.controls.ManfredController;
import manfred.game.controls.SleepingController;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.exception.ManfredException;
import manfred.game.graphics.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.Door;
import manfred.game.interact.Interactable;
import manfred.game.interact.Portal;
import manfred.game.interact.person.Person;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.Map;
import manfred.game.map.MapFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Stack;

import static helpers.AttackCombinationHelper.attackCombination;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManfredControllerTest extends ControllerTestCase {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private Manfred manfred;
    private MapFacade mapFacadeMock;
    private SkillSet skillSet;
    private AttacksContainer attacksContainer;
    private TestGameConfig testGameConfig;
    private BackgroundScroller backgroundScrollerMock;

    private ManfredController underTest;

    @BeforeEach
    void init() {
        testGameConfig = (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE);

        MapCollider colliderMock = mock(MapCollider.class);
        when(colliderMock.collides(0, 0, 0, 0)).thenReturn(true);

        Map mapMock = mock(Map.class);
        when(mapMock.stepOn(any())).thenReturn(null);
        mapFacadeMock = mock(MapFacade.class);
        when(mapFacadeMock.getMap()).thenReturn(mapMock);

        skillSet = new SkillSet();
        attacksContainer = new AttacksContainer();
        backgroundScrollerMock = mock(BackgroundScroller.class);

        manfred = new Manfred(10, 0, 0, PIXEL_BLOCK_SIZE, PIXEL_BLOCK_SIZE, 1, colliderMock, testGameConfig, null);

        CastModeOn castModeOn = new CastModeOn(skillSet, attacksContainer, testGameConfig, manfred.getSprite(), null);
        Caster attackCaster = new Caster(new CastModeOff(castModeOn));
        EnemiesWrapper enemiesWrapper = new EnemiesWrapper();

        underTest = new ManfredController(
            manfred,
            attackCaster,
            mapFacadeMock,
            testGameConfig,
            backgroundScrollerMock,
            mock(GamePanel.class),
            attacksContainer,
            enemiesWrapper,
            mock(GelaberOverlay.class)
        );
    }

    @Test
    void movesRightAndStops() {
        int initialX = manfred.getX();
        int initialY = manfred.getY();

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_D);

        underTest.keyPressed(eventMock);
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

        underTest.keyPressed(eventMock);
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

        underTest.keyPressed(eventMock);
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

        underTest.keyPressed(eventMock);
        manfred.move();

        assertSame(initialX, manfred.getX());
        assertTrue(initialY < manfred.getY());

        assertStops(eventMock, manfred.getX(), manfred.getY());
    }

    private void assertStops(KeyEvent eventMock, int afterMoveX, int afterMoveY) {
        underTest.keyReleased(eventMock);
        manfred.move();

        assertSame(afterMoveX, manfred.getX());
        assertSame(afterMoveY, manfred.getY());
    }

    @Test
    void interactDoesNothingWhenNoInteractInReach() {
        Map map = TestMapFactory.create(new String[][]{{"1", "1"}}, null);
        when(mapFacadeMock.getMap()).thenReturn(map);

        ControllerInterface controllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(controllerState instanceof ManfredController);
    }

    @Test
    void talkToPerson() {
        setupMapWithInteractable(new Person("testOpa", mock(GelaberFacade.class), testGameConfig, null));

        ControllerInterface controllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(controllerState instanceof GelaberController);
    }

    @Test
    void interactWithDoor() throws ManfredException, InterruptedException, InvalidInputException {
        String targetName = "target";
        int targetSpawnX = 5;
        int targetSpawnY = 66;
        setupMapWithInteractable(new Door(targetName, targetSpawnX, targetSpawnY, null));

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_ENTER);
        ControllerInterface controllerState = underTest.keyReleased(eventMock);

        assertTrue(controllerState instanceof SleepingController);
        assertTrue(controllerState.keyPressed(eventMock) instanceof SleepingController);

        Thread.sleep(1000); // Wait for swing worker to finish

        assertTrue(controllerState.keyPressed(eventMock) instanceof ManfredController);

        verify(mapFacadeMock).loadMap(targetName);
        verify(backgroundScrollerMock).centerTo(manfred.getSprite().getCenter());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnX, manfred.getX());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnY, manfred.getY());
    }

    @Test
    void stepOnPortal() throws InterruptedException, ManfredException, InvalidInputException {
        String targetName = "target";
        int targetSpawnX = 5;
        int targetSpawnY = 66;
        setupMapWithInteractable(new Portal(targetName, targetSpawnX, targetSpawnY));

        manfred.setY(PIXEL_BLOCK_SIZE);
        ControllerInterface controllerState = underTest.move();

        assertTrue(controllerState instanceof SleepingController);
        assertTrue(controllerState.keyPressed(mockEventWithKey(KeyEvent.VK_ENTER)) instanceof SleepingController);

        Thread.sleep(1000); // Wait for swing worker to finish

        assertTrue(controllerState.keyPressed(mockEventWithKey(KeyEvent.VK_ENTER)) instanceof ManfredController);

        verify(mapFacadeMock).loadMap(targetName);
        verify(backgroundScrollerMock).centerTo(manfred.getSprite().getCenter());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnX, manfred.getX());
        assertEquals(PIXEL_BLOCK_SIZE * targetSpawnY, manfred.getY());
    }

    @Test
    void givenCorrectCombination_thenTriggersAttack() {
        Attack attackMock = mockSkillSetWithCombination(attackCombination(CombinationElement.LEFT));

        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));
        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_LEFT));
        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));

        assertFalse(attacksContainer.isEmpty());
        assertEquals(attackMock, attacksContainer.getPaintableContainerElements().pop().getPaintable());
    }

    @Test
    void givenWrongCombination_thenDoesNotTriggerAttack() {
        mockSkillSetWithCombination(attackCombination(CombinationElement.LEFT, CombinationElement.LEFT));

        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));
        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_LEFT));
        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));

        assertTrue(attacksContainer.isEmpty());
    }

    private Attack mockSkillSetWithCombination(Stack<CombinationElement> combination) {
        Attack attackMock = mock(Attack.class);
        AttackGenerator attacksGeneratorMock = mock(AttackGenerator.class);
        when(attacksGeneratorMock.generate(any(), any())).thenReturn(attackMock);

        skillSet.put(combination, attacksGeneratorMock);
        return attackMock;
    }

    private void setupMapWithInteractable(Interactable interactable) {
        HashMap<String, Interactable> interactables = new HashMap<>();
        interactables.put("interactable", interactable);

        Map map = TestMapFactory.create(new String[][]{{"1", "interactable"}}, interactables);
        when(mapFacadeMock.getMap()).thenReturn(map);
    }
}