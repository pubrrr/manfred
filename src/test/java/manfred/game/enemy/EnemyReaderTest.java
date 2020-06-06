package manfred.game.enemy;

import manfred.game.characters.MapCollider;
import manfred.game.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnemyReaderTest {
    private EnemyReader underTest;

    @BeforeEach
    void init() {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);
        underTest = new EnemyReader(mapColliderMock);
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