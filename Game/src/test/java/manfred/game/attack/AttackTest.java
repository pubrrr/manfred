package manfred.game.attack;

import helpers.TestGameConfig;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.AnimatedSprite;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.enemy.Enemy;
import org.junit.jupiter.api.Test;

import static helpers.TestMapFactory.coordinateAt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class AttackTest {
    private static final int PIXEL_BLOCK_SIZE = 40;
    private static final Velocity VELOCITY_ZERO = Velocity.withSpeed(PositiveInt.of(0));

    private Attack underTest;

    @Test
    void notResolvedWhenNothingHappens() {
        underTest = new Attack(VELOCITY_ZERO, coordinateAt(0, 0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), mock(AnimatedSprite.class));

        assertFalse(underTest.isResolved());
    }

    @Test
    void resolvesOnTravelledTooFar() {
        PositiveInt range = PositiveInt.of(5);
        Velocity speed = Velocity.withSpeed(PositiveInt.of(2 * range.value()));

        underTest = new Attack(speed, coordinateAt(0, 0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), range, mock(AnimatedSprite.class));

        underTest.up();
        underTest.checkCollisionsAndMove(area -> true);
        assertTrue(underTest.isResolved());
    }

    @Test
    void resolvesOnCollision() {
        underTest = new Attack(VELOCITY_ZERO, coordinateAt(0, 0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(0), mock(AnimatedSprite.class));

        underTest.checkCollisionsAndMove(area -> false);
        assertTrue(underTest.isResolved());
    }

    @Test
    void givenEnemySpriteThatIntersects_thenDamagesAndResolves() {
        int damage = 3;
        int healthPoints = 2;

        underTest = new Attack(VELOCITY_ZERO, coordinateAt(0, 0), PositiveInt.of(5), PositiveInt.of(5), PositiveInt.of(damage), PositiveInt.of(0), mock(AnimatedSprite.class));
        Enemy enemy = new Enemy("test", VELOCITY_ZERO, coordinateAt(3, 3), PositiveInt.of(healthPoints), PositiveInt.ofNonZero(1), (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE), mock(SimpleSprite.class));

        underTest.checkHit(enemy);

        assertEquals(healthPoints - damage, enemy.getHealthPoints());
        assertTrue(underTest.isResolved());
    }

    @Test
    void givenEnemySpriteThatNotIntersects_thenDoesNotDamageAndResolve() {
        int damage = 3;
        int healthPoints = 2;

        underTest = new Attack(VELOCITY_ZERO, coordinateAt(0, 0), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(damage), PositiveInt.of(0), mock(AnimatedSprite.class));
        Enemy enemy = new Enemy("test", VELOCITY_ZERO, coordinateAt(8, 8), PositiveInt.of(healthPoints), PositiveInt.ofNonZero(1), (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE), mock(SimpleSprite.class));

        underTest.checkHit(enemy);

        assertEquals(healthPoints, enemy.getHealthPoints());
        assertFalse(underTest.isResolved());
    }
}