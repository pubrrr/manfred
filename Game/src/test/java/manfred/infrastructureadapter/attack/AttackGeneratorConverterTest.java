package manfred.infrastructureadapter.attack;

import manfred.data.persistence.dto.AttackDto;
import manfred.data.shared.PositiveInt;
import manfred.game.attack.Attack;
import manfred.game.attack.AttackGenerator;
import manfred.game.characters.Direction;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static helpers.TestMapFactory.coordinateAt;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttackGeneratorConverterTest {

    private AttackGeneratorConverter underTest;

    @BeforeEach
    void init() {
        underTest = new AttackGeneratorConverter();
    }

    @Test
    void convert() throws Exception {
        AttackDto input = getInput();

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(coordinateAt(0, 0), Direction.RIGHT);
        assertMoves(attack);
    }

    private AttackDto getInput() {
        return new AttackDto("testName", PositiveInt.of(1), PositiveInt.of(0), PositiveInt.of(0), PositiveInt.of(100), PositiveInt.of(2), PositiveInt.of(3), List.of());
    }

    private void assertMoves(Attack attack) {
        attack.checkCollisionsAndMove(area -> true);
        MatcherAssert.assertThat(attack.getBottomLeft(), is(coordinateAt(1, 0)));
    }

    @Test
    void convertRange() throws Exception {
        AttackDto input = getInput();

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(coordinateAt(0, 0), Direction.RIGHT);

        assertFalse(attack.isResolved());
        attack.checkCollisionsAndMove(area -> true);
        assertFalse(attack.isResolved());
        attack.checkCollisionsAndMove(area -> true);
        assertTrue(attack.isResolved());
    }
}