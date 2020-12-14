package manfred.game.characters;

import manfred.game.attack.AttackGenerator;
import manfred.game.attack.CombinationElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Stack;

import static helpers.AttackCombinationHelper.attackCombination;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class SkillSetTest {

    private SkillSet underTest;

    @BeforeEach
    void setUp() {
        underTest = new SkillSet();
    }

    @Test
    void putAndGet() {
        AttackGenerator attackGenerator = mock(AttackGenerator.class);
        Stack<CombinationElement> putKey = attackCombination(CombinationElement.DOWN);

        underTest.put(putKey, attackGenerator);

        Stack<CombinationElement> getKeyUp = attackCombination(CombinationElement.UP);
        Optional<AttackGenerator> resultUp = underTest.get(getKeyUp);
        assertTrue(resultUp.isEmpty());

        Stack<CombinationElement> getKeyDown = attackCombination(CombinationElement.DOWN);
        Optional<AttackGenerator> resultDown = underTest.get(getKeyDown);
        assertSame(attackGenerator, resultDown.get());
    }

    @Test
    void getDependingOnOrderOfStack() {
        AttackGenerator attackGenerator = mock(AttackGenerator.class);

        underTest.put(
            attackCombination(CombinationElement.DOWN, CombinationElement.LEFT, CombinationElement.RIGHT),
            attackGenerator
        );

        Optional<AttackGenerator> result1 = underTest.get(attackCombination(CombinationElement.DOWN, CombinationElement.LEFT));
        assertTrue(result1.isEmpty());

        Optional<AttackGenerator> result2 = underTest.get(attackCombination(CombinationElement.DOWN, CombinationElement.RIGHT, CombinationElement.LEFT));
        assertTrue(result2.isEmpty());

        Optional<AttackGenerator> result3 = underTest.get(attackCombination(CombinationElement.DOWN, CombinationElement.LEFT, CombinationElement.RIGHT));
        assertSame(attackGenerator, result3.get());
    }
}