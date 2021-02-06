package manfred.infrastructureadapter.enemy;

import helpers.TestGameConfig;
import manfred.data.InvalidInputException;
import manfred.data.persistence.dto.EnemyDto;
import manfred.data.shared.PositiveInt;
import manfred.game.enemy.Enemy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static helpers.TestMapFactory.coordinateAt;
import static helpers.TestMapFactory.defaultCoordinateProvider;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnemyConverterTest {
    private static final int PIXEL_BLOCK_SIZE = 60;

    private EnemyConverter underTest;

    @BeforeEach
    void init() throws InvalidInputException {
        underTest = new EnemyConverter((new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @Test
    void testConvert() {
        int speed = 10;
        EnemyDto input = new EnemyDto("name", PositiveInt.of(100), PositiveInt.of(speed), null);

        Enemy result = underTest.convert(input.at(PositiveInt.of(1), PositiveInt.of(3))).createOnMap(defaultCoordinateProvider());

        assertThat(result.getBottomLeft(), is(coordinateAt(PIXEL_BLOCK_SIZE, 3 * PIXEL_BLOCK_SIZE)));
        assertEquals(100, result.getHealthPoints());

        result.right();
        result.checkCollisionsAndMove(area -> true);
        assertThat(result.getBottomLeft(), is(coordinateAt(PIXEL_BLOCK_SIZE + speed, 3 * PIXEL_BLOCK_SIZE)));
    }
}