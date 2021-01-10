package manfred.game.attack;

import manfred.data.attack.AttackDto;
import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;
import manfred.game.characters.Sprite;
import manfred.game.enemy.Enemy;
import manfred.game.enemy.MapColliderProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AttackGeneratorConverterTest {

    private AttackGeneratorConverter underTest;

    @BeforeEach
    void init() throws Exception {
        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mock(MapCollider.class));

        underTest = new AttackGeneratorConverter(mapColliderProviderMock);
    }

    @Test
    void convert() throws Exception {
        AttackDto input = getInput();

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.RIGHT);
        assertMoves(attack);
        assertHitsEnemy(attack);
    }

    private AttackDto getInput() {
        return new AttackDto("testName", 1, 0, 0, 100, 2, 3, List.of());
    }

    private void assertMoves(Attack attack) {
        attack.move();
        assertEquals(1, attack.getX());
        assertEquals(0, attack.getY());
    }

    private void assertHitsEnemy(Attack attack) {
        Sprite enemySpriteMock = mock(Sprite.class);
        when(enemySpriteMock.intersects(any())).thenReturn(true);
        Enemy enemyMock = mock(Enemy.class);
        when(enemyMock.getSprite()).thenReturn(enemySpriteMock);

        attack.checkHit(enemyMock);
        verify(enemyMock).takeDamage(100);
    }

    @Test
    void convertRange() throws Exception {
        AttackDto input = getInput();

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.RIGHT);

        assertFalse(attack.isResolved());
        attack.move();
        assertFalse(attack.isResolved());
        attack.move();
        assertTrue(attack.isResolved());
    }
}