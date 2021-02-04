package manfred.game.attack;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
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

    private MapCollider mapColliderMock;

    @BeforeEach
    void init() throws InvalidInputException {
        mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        underTest = new AttackGenerator(PositiveInt.of(SPEED), PositiveInt.of(SIZE), PositiveInt.of(SIZE), PositiveInt.of(3), PositiveInt.of(4), List.of(), PositiveInt.of(1));
    }

    @Test
    void generateMovingLeft() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.LEFT);

        result.checkCollisionsAndMove(mapColliderMock);
        assertEquals(-SPEED, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void generateMovingRight() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.RIGHT);

        result.checkCollisionsAndMove(mapColliderMock);
        assertEquals(SPEED, result.getX());
        assertEquals(0, result.getY());
    }

    @Test
    void generateMovingUp() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.UP);

        result.checkCollisionsAndMove(mapColliderMock);
        assertEquals(0, result.getX());
        assertEquals(SPEED, result.getY());
    }

    @Test
    void generateMovingDown() {
        Attack result = underTest.generate(new Point(SIZE / 2, SIZE / 2), Direction.DOWN);

        result.checkCollisionsAndMove(mapColliderMock);
        assertEquals(0, result.getX());
        assertEquals(-SPEED, result.getY());
    }
}