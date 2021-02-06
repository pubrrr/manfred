package manfred.game.enemy;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Manfred;
import manfred.game.characters.Velocity;
import manfred.game.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static helpers.TestMapFactory.coordinateAt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemyTest {
    private final static int AGGRO_RADIUS = 100;
    private final static PositiveInt SPEED = PositiveInt.of(20);
    private final static int PIXEL_BLOCK_SIZE = 4;

    private Enemy underTest;

    @BeforeEach
    void init() throws InvalidInputException {
        underTest = new Enemy("name", Velocity.withSpeed(SPEED), coordinateAt(0, 0), PositiveInt.of(100), null, AGGRO_RADIUS, (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @Test
    void givenManfredOutsideAggroRadius_thenDoesNotMove() {
        Map.Coordinate initialBottomLeft = underTest.getBottomLeft();

        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS + 1, 0));
        underTest.checkCollisionsAndMove(area -> true);
        assertThat(underTest.getBottomLeft(), is(initialBottomLeft));

        underTest.determineSpeed(mockManfredAtCoordinates(0, AGGRO_RADIUS + 1));
        underTest.checkCollisionsAndMove(area -> true);
        assertThat(underTest.getBottomLeft(), is(initialBottomLeft));
    }

    @Test
    void movesHorizontallyTowardsManfred() {
        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS, 0));
        underTest.checkCollisionsAndMove(area -> true);

        assertThat(underTest.getBottomLeft(), is(coordinateAt(SPEED.value(), 0)));
    }

    @Test
    void movesVerticallyTowardsManfred() {
        underTest.determineSpeed(mockManfredAtCoordinates(0, AGGRO_RADIUS));
        underTest.checkCollisionsAndMove(area -> true);

        assertThat(underTest.getBottomLeft(), is(coordinateAt(0, SPEED.value())));
    }

    @Test
    void movesDiagonallyTowardsManfred() {
        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS / 2, AGGRO_RADIUS / 2));
        underTest.checkCollisionsAndMove(area -> true);

        assertThat(underTest.getBottomLeft(), is(coordinateAt((int) ((float) SPEED.value() / Math.sqrt(2)), (int) ((float) SPEED.value() / Math.sqrt(2)))));
    }

    @Test
    void movesAtSomeAngleTowardsManfred() {
        underTest.determineSpeed(mockManfredAtCoordinates(AGGRO_RADIUS * 3 / 4, AGGRO_RADIUS / 4));
        underTest.checkCollisionsAndMove(area -> true);

        assertThat(underTest.getBottomLeft(), is(coordinateAt((int) ((float) SPEED.value() * 3 / Math.sqrt(10)), (int) ((float) SPEED.value() / Math.sqrt(10)))));
    }

    private Manfred mockManfredAtCoordinates(int x, int y) {
        Manfred manfredMock = mock(Manfred.class);
        when(manfredMock.getCenter()).thenReturn(coordinateAt(x + PIXEL_BLOCK_SIZE, y + PIXEL_BLOCK_SIZE));
        return manfredMock;
    }
}