package manfred.game.characters;

import helpers.TestMapFactory;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.sprite.DirectionalAnimatedSprite;
import manfred.game.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

class ManfredTest {
    private static final PositiveInt.Strict BLOCK_SIZE = PositiveInt.ofNonZero(60);

    private static final Map coordinateProvider = TestMapFactory.defaultCoordinateProvider();

    private Manfred underTest;

    @BeforeEach
    void init() {
        underTest = new Manfred(
            Velocity.withSpeed(PositiveInt.of(10)),
            coordinateProvider.coordinateAt(0, 0),
            PositiveInt.of(1),
            BLOCK_SIZE,
            BLOCK_SIZE,
            mock(DirectionalAnimatedSprite.class)
        );
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        Map.Coordinate initialBottomLeft = underTest.getBottomLeft();

        underTest.checkCollisionsAndMove(area -> true);

        assertThat(underTest.getBottomLeft(), is(initialBottomLeft));
    }
}