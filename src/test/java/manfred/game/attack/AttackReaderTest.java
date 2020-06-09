package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;
import manfred.game.enemy.Enemy;
import manfred.game.enemy.MapColliderProvider;
import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttackReaderTest {
    private AttackReader underTest;

    @BeforeEach
    void init() throws Exception {
        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mock(MapCollider.class));

        underTest = new AttackReader(mapColliderProviderMock);
    }

    @Test
    void convertSpeed() throws InvalidInputException {
        String input = "{speed: 1, sizeX: 0, sizeY: 0, damage: 100, range: 2}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.right);
        attack.move();
        assertEquals(1, attack.getX());
        assertEquals(0, attack.getY());
    }

    @Test
    void convertDamage() throws InvalidInputException {
        String input = "{speed: 1, sizeX: 0, sizeY: 0, damage: 100, range: 2}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.right);
        Enemy enemyMock = mock(Enemy.class);
        attack.checkHit(enemyMock);
        verify(enemyMock).takeDamage(100);
    }

    @Test
    void convertRange() throws InvalidInputException {
        String input = "{speed: 2, sizeX: 0, sizeY: 0, damage: 100, range: 1}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.right);

        assertFalse(attack.isResolved());
        attack.move();
        assertFalse(attack.isResolved());
        attack.move();
        assertTrue(attack.isResolved());
    }
}