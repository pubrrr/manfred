package manfred.game.attack;

import manfred.game.characters.Direction;
import manfred.game.characters.SkillSet;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.config.GameConfig;
import manfred.game.map.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Stack;

import static helpers.AttackCombinationHelper.attackCombination;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CastModeOnTest {
    private CastModeOn underTest;
    private AttacksContainer attacksContainerMock;
    private SkillSet skillSetMock;

    @BeforeEach
    void setUp() {
        this.attacksContainerMock = mock(AttacksContainer.class);
        this.skillSetMock = mock(SkillSet.class);

        underTest = new CastModeOn(skillSetMock, attacksContainerMock, mock(GameConfig.class), null);
    }

    @Test
    void givenKnownAttackCombination_addsAttackToContainer() {
        Stack<CombinationElement> attackCombination = attackCombination(CombinationElement.LEFT, CombinationElement.LEFT);
        underTest.addToCombination(CombinationElement.LEFT);
        underTest.addToCombination(CombinationElement.LEFT);

        Attack attackMock = mock(Attack.class);
        AttackGenerator attackGeneratorMock = mock(AttackGenerator.class);
        when(attackGeneratorMock.generate(any(), any())).thenReturn(attackMock);

        when(skillSetMock.get(attackCombination)).thenReturn(Optional.of(attackGeneratorMock));

        CastMode result = underTest.cast(mock(Map.Coordinate.class), Direction.RIGHT);

        verify(attacksContainerMock).add(attackMock);
        assertTrue(result instanceof CastModeOff);
    }

    @Test
    void givenUnkownAttackCombination_doesNotAddAttack() {
        Stack<CombinationElement> attackCombination = attackCombination(CombinationElement.LEFT, CombinationElement.LEFT);
        underTest.addToCombination(CombinationElement.LEFT);
        underTest.addToCombination(CombinationElement.LEFT);

        when(skillSetMock.get(attackCombination)).thenReturn(Optional.empty());

        CastMode result = underTest.cast(mock(Map.Coordinate.class), Direction.RIGHT);

        verify(skillSetMock).get(attackCombination);
        verify(attacksContainerMock, never()).add(any());
        assertTrue(result instanceof CastModeOff);
    }

    @Test
    void castTwoAttacks() {
        Stack<CombinationElement> attackCombinationLeft = attackCombination(CombinationElement.LEFT);
        Stack<CombinationElement> attackCombinationRight = attackCombination(CombinationElement.RIGHT);

        Attack attackLeftMock = mock(Attack.class);
        AttackGenerator attackLeftGeneratorMock = mock(AttackGenerator.class);
        when(attackLeftGeneratorMock.generate(any(), any())).thenReturn(attackLeftMock);

        Attack attackRightMock = mock(Attack.class);
        AttackGenerator attackRightGeneratorMock = mock(AttackGenerator.class);
        when(attackRightGeneratorMock.generate(any(), any())).thenReturn(attackRightMock);

        when(skillSetMock.get(attackCombinationLeft)).thenReturn(Optional.of(attackLeftGeneratorMock));
        when(skillSetMock.get(attackCombinationRight)).thenReturn(Optional.of(attackRightGeneratorMock));

        underTest.addToCombination(CombinationElement.LEFT);

        CastMode modeOff = underTest.cast(mock(Map.Coordinate.class), Direction.RIGHT);
        underTest = (CastModeOn) modeOff.cast(mock(Map.Coordinate.class), Direction.RIGHT);

        verify(skillSetMock).get(attackCombinationLeft);
        verify(attacksContainerMock).add(attackLeftMock);
        verify(attacksContainerMock, never()).add(attackRightMock);

        underTest.addToCombination(CombinationElement.RIGHT);
        underTest.cast(mock(Map.Coordinate.class), Direction.RIGHT);

        verify(skillSetMock).get(attackCombinationRight);
        verify(attacksContainerMock).add(attackRightMock);
    }
}