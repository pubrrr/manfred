package manfred.game.attack;

import manfred.game.characters.MapCollider;
import manfred.game.enemy.Enemy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackTest {
    private Attack underTest;

    @Test
    void notResolvedWhenNothingHappens() {
        underTest = new Attack(0, 0, 0, 0, 0, mock(MapCollider.class), 0, 0);

        assertFalse(underTest.isResolved());
    }

    @Test
    void resolvesOnTravelledTooFar() {
        int range = 5;
        int speed = 2 * range;

        underTest = new Attack(speed, 0, 0, 0, 0, mock(MapCollider.class), 0, range);

        underTest.up();
        underTest.move();
        assertTrue(underTest.isResolved());
    }

    @Test
    void resolvesOnCollision() {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        underTest = new Attack(0, 0, 0, 0, 0, mapColliderMock, 0, 0);

        underTest.move();
        assertTrue(underTest.isResolved());
    }

    @Test
    void givenEnemySpriteThatIntersects_thenDamagesAndResolves() {
        int damage = 3;
        int healthPoints = 2;

        underTest = new Attack(0, 0, 0, 5, 5, mock(MapCollider.class), damage, 0);
        Enemy enemy = new Enemy("test", 0, 3, 3, healthPoints, mock(MapCollider.class), 0);

        underTest.checkHit(enemy);

        assertEquals(healthPoints - damage, enemy.getHealthPoints());
        assertTrue(underTest.isResolved());
    }

    @Test
    void givenEnemySpriteThatNotIntersects_thenDoesNotDamageAndResolve() {
        int damage = 3;
        int healthPoints = 2;

        underTest = new Attack(0, 0, 0, 5, 5, mock(MapCollider.class), damage, 0);
        Enemy enemy = new Enemy("test", 0, 8, 8, healthPoints, mock(MapCollider.class), 0);

        underTest.checkHit(enemy);

        assertEquals(healthPoints, enemy.getHealthPoints());
        assertFalse(underTest.isResolved());
    }
}