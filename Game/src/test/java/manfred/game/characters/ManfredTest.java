package manfred.game.characters;

import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ManfredTest {
    private static final int BLOCK_SIZE = 60;

    private static final Map coordinateProvider = TestMapFactory.defaultCoordinateProvider();

    private Manfred underTest;

    @BeforeEach
    void init() throws InvalidInputException {
        underTest = new Manfred(Velocity.withSpeed(PositiveInt.of(10)), coordinateProvider.coordinateAt(0, 0), PositiveInt.of(BLOCK_SIZE), PositiveInt.of(BLOCK_SIZE), PositiveInt.of(1), (new TestGameConfig()).withPixelBlockSize(BLOCK_SIZE), null);
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        Map.Coordinate initialBottomLeft = underTest.getBottomLeft();

        underTest.checkCollisionsAndMove(area -> true);

        assertThat(underTest.getBottomLeft(), is(initialBottomLeft));
    }
}