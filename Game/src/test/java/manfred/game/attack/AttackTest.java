package manfred.game.attack;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.MapCollider;
import manfred.game.characters.Velocity;
import manfred.game.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackTest {
    private static final int PIXEL_BLOCK_SIZE = 40;
    private static final Velocity VELOCITY_ZERO = Velocity.withSpeed(PositiveInt.of(0));

    private Attack underTest;

    private MapCollider mapColliderMock;

    @BeforeEach
    void setUp() {
        mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);
    }

    @Test
    void notResolvedWhenNothingHappens() throws InvalidInputException {
        underTest = new Attack(VELOCITY_ZERO, 0, 0, PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), null, PositiveInt.of(1));

        assertFalse(underTest.isResolved());
    }

    @Test
    void resolvesOnTravelledTooFar() throws InvalidInputException {
        PositiveInt range = PositiveInt.of(5);
        Velocity speed = Velocity.withSpeed(PositiveInt.of(2 * range.value()));

        underTest = new Attack(speed, 0, 0, PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), range, null, PositiveInt.of(1));

        underTest.up();
        underTest.checkCollisionsAndMove(mapColliderMock);
        assertTrue(underTest.isResolved());
    }

    @Test
    void resolvesOnCollision() throws InvalidInputException {
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        underTest = new Attack(VELOCITY_ZERO, 0, 0, PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), null, PositiveInt.of(1));

        underTest.checkCollisionsAndMove(mapColliderMock);
        assertTrue(underTest.isResolved());
    }

    @Test
    void givenEnemySpriteThatIntersects_thenDamagesAndResolves() throws InvalidInputException {
        int damage = 3;
        int healthPoints = 2;

        underTest = new Attack(VELOCITY_ZERO, 0, 0, PositiveInt.of(5), PositiveInt.of(5), PositiveInt.of(damage), PositiveInt.of(0), null, PositiveInt.of(1));
        Enemy enemy = new Enemy("test", VELOCITY_ZERO, 3, 3, PositiveInt.of(healthPoints), null, 0, (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));

        underTest.checkHit(enemy);

        assertEquals(healthPoints - damage, enemy.getHealthPoints());
        assertTrue(underTest.isResolved());
    }

    @Test
    void givenEnemySpriteThatNotIntersects_thenDoesNotDamageAndResolve() throws InvalidInputException {
        int damage = 3;
        int healthPoints = 2;

        underTest = new Attack(VELOCITY_ZERO, 0, 0, PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(damage), PositiveInt.of(0), null, PositiveInt.of(1));
        Enemy enemy = new Enemy("test", VELOCITY_ZERO, 8, 8, PositiveInt.of(healthPoints), null, 0, (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));

        underTest.checkHit(enemy);

        assertEquals(healthPoints, enemy.getHealthPoints());
        assertFalse(underTest.isResolved());
    }
}