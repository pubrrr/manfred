package manfred.game.characters;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import manfred.data.shared.PositiveInt;
import manfred.game.geometry.Vector;
import manfred.game.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class VelocityTest {
    private final static PositiveInt SPEED = PositiveInt.of(1000);
    public static final PositiveInt.Strict LENGTH = PositiveInt.ofNonZero(100);

    private Velocity underTest;

    @BeforeEach
    void setUp() {
        underTest = Velocity.withSpeed(SPEED);
    }

    @Test
    void initiallyZero() {
        assertThat(underTest.getVector().length().value(), is(0));
    }

    @TestTemplate
    @UseDataProvider("provideDirections")
    void accelerateOnce(Direction direction) {
        Velocity result = underTest.accelerate(direction);

        assertThat(result.getVector().length(), is(SPEED));
        Vector<Map.Coordinate> directionVector = direction.getUnitVector().scaleToLength(LENGTH);
        Vector<Map.Coordinate> scaledDirectionVector = directionVector.scale(SPEED, PositiveInt.ofNonZero(directionVector.length().value()));
        assertThat(result.getVector(), is(scaledDirectionVector));
    }

    @TestTemplate
    @UseDataProvider("provideDirections")
    void accelerateTwiceInTheSameDirection(Direction direction) {
        Velocity result = underTest.accelerate(direction).accelerate(direction);

        assertThat(result.getVector().length(), is(SPEED));
        Vector<Map.Coordinate> directionVector = direction.getUnitVector().scaleToLength(LENGTH);
        Vector<Map.Coordinate> scaledDirectionVector = directionVector.scale(SPEED, PositiveInt.ofNonZero(directionVector.length().value()));
        assertThat(result.getVector(), is(scaledDirectionVector));
    }

    @DataProvider
    static Object[][] provideDirections() {
        return new Object[][]{
            new Object[]{Direction.UP},
            new Object[]{Direction.DOWN},
            new Object[]{Direction.LEFT},
            new Object[]{Direction.RIGHT}
        };
    }

    @TestTemplate
    @UseDataProvider("provideOppositeDirections")
    void accelerateAndStop(Direction direction, Direction oppositeDirection) {
        Velocity result = underTest.accelerate(direction).accelerate(oppositeDirection);

        assertThat(result.getVector().length().value(), is(0));
    }

    @TestTemplate
    @UseDataProvider("provideOppositeDirections")
    void accelerateTwiceAndStop(Direction direction, Direction oppositeDirection) {
        Velocity result = underTest.accelerate(direction).accelerate(direction).accelerate(oppositeDirection);

        assertThat(result.getVector().length().value(), is(0));
    }

    @DataProvider
    static Object[][] provideOppositeDirections() {
        return new Object[][]{
            new Object[]{Direction.UP, Direction.DOWN},
            new Object[]{Direction.DOWN, Direction.UP},
            new Object[]{Direction.LEFT, Direction.RIGHT},
            new Object[]{Direction.RIGHT, Direction.LEFT},
        };
    }

    @TestTemplate
    @UseDataProvider("provideOrthogonalDirections")
    void accelerateTwiceInOrthogonalDirections(Direction direction1, Direction direction2) {
        Velocity result = underTest.accelerate(direction1).accelerate(direction2);

        assertThat(result.getVector().length().value(), greaterThanOrEqualTo(SPEED.value() - 5));
        assertThat(result.getVector().length().value(), lessThanOrEqualTo(SPEED.value() + 5));
        Vector<Map.Coordinate> directionVector = direction1.getUnitVector().scaleToLength(LENGTH).add(direction2.getUnitVector().scaleToLength(LENGTH));
        Vector<Map.Coordinate> scaledDirectionVector = directionVector.scale(SPEED, PositiveInt.ofNonZero(directionVector.length().value()));
        assertThat(result.getVector(), equalTo(scaledDirectionVector));
    }

    @TestTemplate
    @UseDataProvider("provideOrthogonalDirections")
    void accelerateInADirectionInAnotherDirectionAndInTheFirstDirectionAgain(Direction direction1, Direction direction2) {
        Velocity result = underTest.accelerate(direction1).accelerate(direction2).accelerate(direction1);

        assertThat(result.getVector().length().value(), greaterThanOrEqualTo(SPEED.value() - 5));
        assertThat(result.getVector().length().value(), lessThanOrEqualTo(SPEED.value() + 5));
        Vector<Map.Coordinate> directionVector = direction1.getUnitVector().scaleToLength(LENGTH).add(direction2.getUnitVector().scaleToLength(LENGTH));
        Vector<Map.Coordinate> scaledDirectionVector = directionVector.scale(SPEED, PositiveInt.ofNonZero(directionVector.length().value()));
        assertThat(result.getVector(), equalTo(scaledDirectionVector));
    }

    @TestTemplate
    @UseDataProvider("provideOrthogonalDirections")
    void accelerateInADirectionAndTwiceAnotherDirection(Direction direction1, Direction direction2) {
        Velocity result = underTest.accelerate(direction1).accelerate(direction2).accelerate(direction2);

        assertThat(result.getVector().length().value(), greaterThanOrEqualTo(SPEED.value() - 3));
        assertThat(result.getVector().length().value(), lessThanOrEqualTo(SPEED.value() + 3));
        Vector<Map.Coordinate> directionVector = direction1.getUnitVector().scaleToLength(LENGTH).add(direction2.getUnitVector().scaleToLength(LENGTH));
        Vector<Map.Coordinate> scaledDirectionVector = directionVector.scale(SPEED, PositiveInt.ofNonZero(directionVector.length().value()));
        assertThat(result.getVector(), equalTo(scaledDirectionVector));
    }

    @DataProvider
    static Object[][] provideOrthogonalDirections() {
        return new Object[][]{
            new Object[]{Direction.UP, Direction.LEFT},
            new Object[]{Direction.UP, Direction.RIGHT},
            new Object[]{Direction.DOWN, Direction.LEFT},
            new Object[]{Direction.DOWN, Direction.RIGHT},
            new Object[]{Direction.LEFT, Direction.UP},
            new Object[]{Direction.LEFT, Direction.DOWN},
            new Object[]{Direction.RIGHT, Direction.UP},
            new Object[]{Direction.RIGHT, Direction.DOWN}
        };
    }

    @TestTemplate
    @UseDataProvider("provideOrthogonalDirectionsSequence")
    void accelerateInADirectionInAnotherDirectionAndInTheOppositeDirection(Direction direction1, Direction anotherDirection, Direction oppositeDirection) {
        Velocity result = underTest.accelerate(direction1).accelerate(anotherDirection).accelerate(oppositeDirection);

        assertThat(result.getVector().length(), is(SPEED));
        Vector<Map.Coordinate> directionVector = direction1.getUnitVector().scaleToLength(LENGTH);
        Vector<Map.Coordinate> scaledDirectionVector = directionVector.scale(SPEED, PositiveInt.ofNonZero(directionVector.length().value()));
        assertThat(result.getVector(), is(scaledDirectionVector));
    }

    @DataProvider
    static Object[][] provideOrthogonalDirectionsSequence() {
        return new Object[][]{
            new Object[]{Direction.UP, Direction.LEFT, Direction.RIGHT},
            new Object[]{Direction.UP, Direction.RIGHT, Direction.LEFT},
            new Object[]{Direction.DOWN, Direction.LEFT, Direction.RIGHT},
            new Object[]{Direction.DOWN, Direction.RIGHT, Direction.LEFT},
            new Object[]{Direction.LEFT, Direction.UP, Direction.DOWN},
            new Object[]{Direction.LEFT, Direction.DOWN, Direction.UP},
            new Object[]{Direction.RIGHT, Direction.UP, Direction.DOWN},
            new Object[]{Direction.RIGHT, Direction.DOWN, Direction.UP}
        };
    }

    @Test
    void moveInOddDirections() {
        Vector<Map.Coordinate> direction1 = Vector.of(30, 70);
        Velocity result1 = underTest.moveInDirection(direction1);

        assertThat(result1.getVector().x(), lessThan(result1.getVector().y()));
        assertThat(result1.getVector().length().value(), greaterThanOrEqualTo(SPEED.value() - 3));
        assertThat(result1.getVector().length().value(), lessThanOrEqualTo(SPEED.value() + 3));

        Vector<Map.Coordinate> direction2 = Vector.of(30, -20);
        Velocity result2 = result1.moveInDirection(direction2);

        assertThat(result2.getVector().x(), greaterThan(Math.abs(result2.getVector().y())));
        assertThat(result2.getVector().length().value(), greaterThanOrEqualTo(SPEED.value() - 3));
        assertThat(result2.getVector().length().value(), lessThanOrEqualTo(SPEED.value() + 3));

        Velocity stop = result2.stop();
        assertThat(stop.getVector().length().value(), is(0));
    }
}