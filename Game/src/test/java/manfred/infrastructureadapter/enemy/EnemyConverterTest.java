package manfred.infrastructureadapter.enemy;

import helpers.TestGameConfig;
import manfred.data.persistence.dto.EnemyDto;
import manfred.game.characters.MapCollider;
import manfred.game.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemyConverterTest {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private EnemyConverter underTest;

    private MapCollider mapColliderMock;

    @BeforeEach
    void init() {
        mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        underTest = new EnemyConverter((new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @Test
    void testConvert() {
        int speed = 10;
        EnemyDto input = new EnemyDto("name", 100, speed, null);

        Enemy result = underTest.convert(input.at(1, 22));

        assertEquals(PIXEL_BLOCK_SIZE, result.getX());
        assertEquals(PIXEL_BLOCK_SIZE * 22, result.getY());
        assertEquals(100, result.getHealthPoints());

        result.right();
        result.checkCollisionsAndMove(mapColliderMock);
        assertEquals(PIXEL_BLOCK_SIZE + speed, result.getX());
    }
}