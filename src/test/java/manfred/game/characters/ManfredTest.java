package manfred.game.characters;

import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.DataProviderExtension;
import com.tngtech.junit.dataprovider.UseDataProvider;
import com.tngtech.junit.dataprovider.UseDataProviderExtension;
import helpers.TestMapFactory;
import manfred.game.attack.Attack;
import manfred.game.attack.AttackGenerator;
import manfred.game.attack.AttacksContainer;
import manfred.game.controls.KeyControls;
import manfred.game.graphics.GamePanel;
import manfred.game.map.Accessible;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.game.map.MapWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Stack;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DataProviderExtension.class)
@ExtendWith(UseDataProviderExtension.class)
class ManfredTest {
    private Manfred underTest;

    private MapWrapper mapWrapperMock;
    private AttacksContainer attacksContainerMock;
    private SkillSet skillSetMock;

    @BeforeEach
    void init() {
        MapCollider colliderMock = mock(MapCollider.class);
        mapWrapperMock = mock(MapWrapper.class);
        attacksContainerMock = mock(AttacksContainer.class);
        skillSetMock = mock(SkillSet.class);

        underTest = new Manfred(10, 0, 0, 1, colliderMock, mapWrapperMock, attacksContainerMock, skillSetMock);
    }

    @Test
    void whenNoKeyPressed_thenDoesNotMove() {
        Map map = TestMapFactory.create(new String[][]{{"1"}}, null);
        when(mapWrapperMock.getMap()).thenReturn(map);

        int initialX = underTest.getX();
        int initialY = underTest.getY();

        underTest.move();

        assertEquals(initialX, underTest.getX());
        assertEquals(initialY, underTest.getY());
    }

    @TestTemplate
    @UseDataProvider("provideInitialManfredCoordinates")
    public void triggerOnStep(int manfredX, int manfredY, boolean expectTriggerStepOn) {
        underTest.setX(manfredX);
        underTest.setY(manfredY);

        Consumer<KeyControls> nonZeroLambda = keyControls -> {};
        setup4x4MapMockWithOnStepCallbackTopLeftTile(nonZeroLambda);

        Consumer<KeyControls> result = underTest.move();

        assertEquals(expectTriggerStepOn, result != null);
    }

    @DataProvider
    static Object[][] provideInitialManfredCoordinates() {
        int halfBlockSize = GamePanel.PIXEL_BLOCK_SIZE / 2;
        return new Object[][]{
                {0, 0, true},
                {halfBlockSize - 1, 0, true},
                {0, halfBlockSize - 1, true},
                {halfBlockSize - 1, halfBlockSize - 1, true},
                {halfBlockSize, 0, false},
                {0, halfBlockSize, false},
                {halfBlockSize, halfBlockSize, false},
        };
    }

    private void setup4x4MapMockWithOnStepCallbackTopLeftTile(Consumer<KeyControls> nonZeroLambda) {
        MapTile mapTileMock = mock(MapTile.class);
        when(mapTileMock.onStep()).thenReturn(nonZeroLambda);
        Map map = new Map("test", new MapTile[][]{{mapTileMock, Accessible.getInstance()}, {Accessible.getInstance(), Accessible.getInstance()}});
        when(mapWrapperMock.getMap()).thenReturn(map);
    }

    @Test
    void noGapBetweenTwoNeighboringOnStepTiles() {
        Consumer<KeyControls> nonZeroLambda = keyControls -> {};

        MapTile mapTileMock = mock(MapTile.class);
        when(mapTileMock.onStep()).thenReturn(nonZeroLambda);
        Map map = new Map("test", new MapTile[][]{{mapTileMock, mapTileMock}});
        Map mapSpy = spy(map);
        when(mapWrapperMock.getMap()).thenReturn(mapSpy);

        underTest.down();

        while (underTest.getY() <= GamePanel.PIXEL_BLOCK_SIZE) {
            Consumer<KeyControls> result = underTest.move();
            assertNotNull(result);
        }
        verify(mapSpy, atLeastOnce()).stepOn(0, 0);
        verify(mapSpy, atLeastOnce()).stepOn(0, 1);
    }

    @Test
    void givenKnownAttackCombination_addsAttackToContainer() {
        Stack<String> attackCombination = new Stack<>();
        attackCombination.push("a");
        attackCombination.push("b");
        attackCombination.push("c");

        Attack attackMock = mock(Attack.class);
        AttackGenerator attackGeneratorMock = mock(AttackGenerator.class);
        when(attackGeneratorMock.generate()).thenReturn(attackMock);

        when(skillSetMock.get("abc")).thenReturn(attackGeneratorMock);

        underTest.cast(attackCombination);

        verify(attacksContainerMock).add(attackMock);
    }

    @Test
    void givenUnkownAttackCombination_doesNotAddAttack() {
        Stack<String> attackCombination = new Stack<>();
        attackCombination.push("a");
        attackCombination.push("b");
        attackCombination.push("c");

        when(skillSetMock.get("abc")).thenReturn(null);

        underTest.cast(attackCombination);

        verify(attacksContainerMock, never()).add(any());
    }
}