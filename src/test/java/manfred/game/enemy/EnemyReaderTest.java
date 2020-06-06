package manfred.game.enemy;

import manfred.game.characters.MapCollider;
import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EnemyReaderTest {
    private EnemyReader underTest;

    @BeforeEach
    void init() throws Exception {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mapColliderMock);

        underTest = new EnemyReader(mapColliderProviderMock);
    }

    @Test
    void testConvert() throws InvalidInputException {
        String input = "{name: testName, healthPoints: 100, speed: 1}";

        Enemy result = underTest.convert(input, 1, 22);

        assertEquals(1, result.getX());
        assertEquals(22, result.getY());
        assertEquals(100, result.getHealthPoints());

        result.right();
        result.move();
        assertEquals(2, result.getX());
    }
}