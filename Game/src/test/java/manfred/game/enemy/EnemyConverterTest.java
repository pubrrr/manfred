package manfred.game.enemy;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.enemy.EnemyDto;
import manfred.game.characters.MapCollider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EnemyConverterTest {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private EnemyConverter underTest;

    @BeforeEach
    void init() throws Exception {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mapColliderMock);

        underTest = new EnemyConverter(mapColliderProviderMock, (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @Test
    void testConvert() throws InvalidInputException {
        int speed = 10;
        EnemyDto input = new EnemyDto("name", 100, speed, null);

        Enemy result = underTest.convert(input, 1, 22);

        assertEquals(PIXEL_BLOCK_SIZE, result.getX());
        assertEquals(PIXEL_BLOCK_SIZE * 22, result.getY());
        assertEquals(100, result.getHealthPoints());

        result.right();
        result.move();
        assertEquals(PIXEL_BLOCK_SIZE + speed, result.getX());
    }
}