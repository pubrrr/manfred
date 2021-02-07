package componentTests.controller;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.attack.Attack;
import manfred.game.attack.AttackGenerator;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.CastModeOff;
import manfred.game.attack.CastModeOn;
import manfred.game.attack.Caster;
import manfred.game.attack.CombinationElement;
import manfred.game.characters.Manfred;
import manfred.game.characters.SkillSet;
import manfred.game.characters.Velocity;
import manfred.game.config.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.GelaberController;
import manfred.game.controls.ManfredController;
import manfred.game.controls.ObjectsMover;
import manfred.game.controls.SleepingController;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.graphics.BackgroundScroller;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.interact.Door;
import manfred.game.interact.Portal;
import manfred.game.interact.person.Person;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.Map;
import manfred.game.map.MapFacade;
import manfred.game.map.MapTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

import static helpers.AttackCombinationHelper.attackCombination;
import static helpers.TestMapFactory.coordinateAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ManfredControllerTest extends ControllerTestCase {
    private static final int PIXEL_BLOCK_SIZE = 40;
    private static final int INITIAL_X = 20;
    public static final int INITIAL_Y = 20;
    public static final PositiveInt SPEED = PositiveInt.of(10);

    private Manfred manfred;
    private MapFacade mapFacadeMock;
    private SkillSet skillSet;
    private AttacksContainer attacksContainer;
    private BackgroundScroller backgroundScrollerMock;

    private ManfredController underTest;

    @BeforeEach
    void init() {
        TestGameConfig testGameConfig = (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE);

        mapFacadeMock = mock(MapFacade.class);
        when(mapFacadeMock.stepOn(any())).thenReturn(null);

        skillSet = new SkillSet();
        attacksContainer = new AttacksContainer();
        backgroundScrollerMock = mock(BackgroundScroller.class);

        manfred = new Manfred(Velocity.withSpeed(SPEED), coordinateAt(INITIAL_X, INITIAL_Y), PositiveInt.of(PIXEL_BLOCK_SIZE), PositiveInt.of(PIXEL_BLOCK_SIZE), PositiveInt.of(1), testGameConfig, null);

        CastModeOn castModeOn = new CastModeOn(skillSet, attacksContainer, mock(GameConfig.class), manfred.getSprite(), null);
        Caster attackCaster = new Caster(new CastModeOff(castModeOn));
        EnemiesWrapper enemiesWrapper = new EnemiesWrapper();

        ObjectsMover objectsMover = new ObjectsMover(mapFacadeMock, manfred, enemiesWrapper, attacksContainer);

        underTest = new ManfredController(
            manfred,
            attackCaster,
            mapFacadeMock,
            backgroundScrollerMock,
            mock(GamePanel.class),
            attacksContainer,
            mock(GelaberOverlay.class),
            objectsMover,
            new MapCoordinateToPanelCoordinateTransformer(PositiveInt.ofNonZero(PIXEL_BLOCK_SIZE))
        );
    }

    @Test
    void movesRightAndStops() {
        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_D);

        underTest.keyPressed(eventMock);
        manfred.checkCollisionsAndMove(area -> true);

        assertThat(manfred.getBottomLeft(), is(coordinateAt(INITIAL_X + SPEED.value(), INITIAL_Y)));

        assertStops(eventMock, manfred.getBottomLeft());
    }

    @Test
    void movesLeftAndStops() {
        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_A);

        underTest.keyPressed(eventMock);
        manfred.checkCollisionsAndMove(area -> true);

        assertThat(manfred.getBottomLeft(), is(coordinateAt(INITIAL_X - SPEED.value(), INITIAL_Y)));

        assertStops(eventMock, manfred.getBottomLeft());
    }

    @Test
    void movesUpAndStops() {
        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_W);

        underTest.keyPressed(eventMock);
        manfred.checkCollisionsAndMove(area -> true);

        assertThat(manfred.getBottomLeft(), is(coordinateAt(INITIAL_X, INITIAL_Y + SPEED.value())));

        assertStops(eventMock, manfred.getBottomLeft());
    }

    @Test
    void movesDownAndStops() {
        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_S);

        underTest.keyPressed(eventMock);
        manfred.checkCollisionsAndMove(area -> true);

        assertThat(manfred.getBottomLeft(), is(coordinateAt(INITIAL_X, INITIAL_Y - SPEED.value())));

        assertStops(eventMock, manfred.getBottomLeft());
    }

    private void assertStops(KeyEvent eventMock, Map.Coordinate before) {
        underTest.keyReleased(eventMock);
        manfred.checkCollisionsAndMove(area -> true);

        assertThat(manfred.getBottomLeft(), equalTo(before));
    }

    @Test
    void interactDoesNothingWhenNoInteractInReach() {
        when(mapFacadeMock.interactWithTile(any())).thenReturn(ControllerStateMapper::preserveState);

        ControllerInterface controllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(controllerState instanceof ManfredController);
    }

    @Test
    void talkToPerson() {
        setupMapWithInteractable(new Person("testOpa", mock(GelaberFacade.class), mock(GameConfig.class), new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));

        ControllerInterface controllerState = underTest.keyReleased(mockEventWithKey(KeyEvent.VK_ENTER));

        assertTrue(controllerState instanceof GelaberController);
    }

    @Test
    void interactWithDoor() throws InterruptedException, InvalidInputException {
        String targetName = "target";
        PositiveInt targetSpawnX = PositiveInt.of(2);
        PositiveInt targetSpawnY = PositiveInt.of(3);
        setupMapWithInteractable(new Door(targetName, targetSpawnX, targetSpawnY));

        KeyEvent eventMock = mockEventWithKey(KeyEvent.VK_ENTER);
        ControllerInterface controllerState = underTest.keyReleased(eventMock);

        assertTrue(controllerState instanceof SleepingController);
        assertTrue(controllerState.keyPressed(eventMock) instanceof SleepingController);

        Thread.sleep(1000); // Wait for swing worker to finish

        assertTrue(controllerState.keyPressed(eventMock) instanceof ManfredController);
        verify(mapFacadeMock).loadMap(eq(targetName), any());
        verify(backgroundScrollerMock).centerTo(any());
    }

    @Test
    void stepOnPortal() throws InterruptedException, InvalidInputException {
        String targetName = "target";
        PositiveInt targetSpawnX = PositiveInt.of(5);
        PositiveInt targetSpawnY = PositiveInt.of(66);
        setupMapWithInteractable(new Portal(targetName, targetSpawnX, targetSpawnY));

        ControllerInterface controllerState = underTest.move();

        assertTrue(controllerState instanceof SleepingController);

        Thread.sleep(1000); // Wait for swing worker to finish

        assertTrue(controllerState.keyPressed(mockEventWithKey(KeyEvent.VK_ENTER)) instanceof ManfredController);

        verify(mapFacadeMock).loadMap(eq(targetName), any());
        verify(backgroundScrollerMock).centerTo(any());
    }

    @Test
    void givenCorrectCombination_thenTriggersAttack() {
        Attack attackMock = mockSkillSetWithCombination(attackCombination(CombinationElement.LEFT));

        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));
        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_LEFT));
        underTest.keyPressed(mockEventWithKey(KeyEvent.VK_SPACE));

        assertFalse(attacksContainer.isEmpty());
        assertEquals(attackMock, attacksContainer.getPaintableContainerElements().pop().getLocatedPaintable());
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

    private void setupMapWithInteractable(MapTile interactable) {
        when(mapFacadeMock.interactWithTile(any())).thenReturn(interactable.interact());
        when(mapFacadeMock.stepOn(any())).thenReturn(interactable.onStep());
    }
}