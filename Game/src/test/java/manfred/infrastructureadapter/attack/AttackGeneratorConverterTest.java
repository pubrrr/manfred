package manfred.infrastructureadapter.attack;

import manfred.data.persistence.dto.AttackDto;
import manfred.game.attack.Attack;
import manfred.game.attack.AttackGenerator;
import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;
import manfred.game.characters.Sprite;
import manfred.game.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AttackGeneratorConverterTest {

    private AttackGeneratorConverter underTest;

    private MapCollider mapColliderMock;

    @BeforeEach
    void init() {
        mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        underTest = new AttackGeneratorConverter();
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
        attack.checkCollisionsAndMove(mapColliderMock);
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
        attack.checkCollisionsAndMove(mapColliderMock);
        assertFalse(attack.isResolved());
        attack.checkCollisionsAndMove(mapColliderMock);
        assertTrue(attack.isResolved());
    }
}