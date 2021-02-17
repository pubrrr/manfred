package manfred.game.attack;

import manfred.data.shared.PositiveInt;
import manfred.game.characters.Direction;
import manfred.game.characters.sprite.AnimatedSprite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static helpers.TestMapFactory.coordinateAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttackGeneratorTest {
    private final static int SPEED = 3;
    private static final int SIZE = 0;
    private static final int INITIAL_Y = 20;
    private static final int INITIAL_X = 30;
    private static final int CAST_DISTANCE = 20;

    AttackGenerator underTest;

    @BeforeEach
    void init() {
        AnimatedSprite spriteMock = mock(AnimatedSprite.class);
        when(spriteMock.buildClone()).thenReturn(spriteMock);
        underTest = new AttackGenerator(PositiveInt.of(SPEED), PositiveInt.of(SIZE), PositiveInt.of(SIZE), PositiveInt.of(3), PositiveInt.of(4), spriteMock);
    }

    @Test
    void generateMovingLeft() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.LEFT);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X - SPEED - CAST_DISTANCE, INITIAL_Y)));
    }

    @Test
    void generateMovingRight() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.RIGHT);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X + SPEED + CAST_DISTANCE, INITIAL_Y)));
    }

    @Test
    void generateMovingUp() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.UP);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X, INITIAL_Y + SPEED + CAST_DISTANCE)));
    }

    @Test
    void generateMovingDown() {
        Attack result = underTest.generate(coordinateAt(INITIAL_X, INITIAL_Y), Direction.DOWN);

        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(INITIAL_X, INITIAL_Y - SPEED - CAST_DISTANCE)));
    }
}