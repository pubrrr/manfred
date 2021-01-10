package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackGeneratorTest {
    private final static int SPEED = 10;
    public static final int SIZE = 5;

    AttackGenerator underTest;

    @BeforeEach
    void init() {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        underTest = new AttackGenerator(SPEED, SIZE, SIZE, mapColliderMock, 3, 4, List.of(), 1);
    }

    @Test
    void generateMovingLeft() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.LEFT);

        result.move();
        assertEquals(-SPEED, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void generateMovingRight() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.RIGHT);

        result.move();
        assertEquals(SPEED, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void generateMovingUp() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.UP);

        result.move();
        assertEquals(0, result.getX());
        assertEquals(-SPEED, result.getY());
    }

    @Test
    void generateMovingDown() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.DOWN);

        result.move();
        assertEquals(0, result.getX());
        assertEquals(SPEED, result.getY());
    }
}