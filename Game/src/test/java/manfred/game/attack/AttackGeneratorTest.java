package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.TestMapFactory.coordinateAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AttackGeneratorTest {
    private final static int SPEED = 3;
    public static final int SIZE = 8;
    public static final int INITIAL_Y = 20;
    public static final int INITIAL_X = 30;

    AttackGenerator underTest;

    @BeforeEach
    void init() {
        underTest = new AttackGenerator(PositiveInt.of(SPEED), PositiveInt.of(SIZE), PositiveInt.of(SIZE), PositiveInt.of(3), PositiveInt.of(4), List.of(), PositiveInt.of(1));
    }

    @Test
    void generateMovingLeft() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.LEFT);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X - SPEED, INITIAL_Y)));
    }

    @Test
    void generateMovingRight() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.RIGHT);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X + SPEED, INITIAL_Y)));
    }

    @Test
    void generateMovingUp() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.UP);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X, INITIAL_Y + SPEED)));
    }

    @Test
    void generateMovingDown() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.DOWN);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X, INITIAL_Y - SPEED)));
    }
}