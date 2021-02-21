package manfred.infrastructureadapter.map;

import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.shared.PositiveInt;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.Enemy;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.game.map.NotAccessible;
import manfred.infrastructureadapter.enemy.EnemyConverter;
import manfred.infrastructureadapter.enemy.EnemyFactory;
import manfred.data.infrastructure.map.TileConversionRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class MapConverterTest {
    private MapConverter underTest;
    private EnemyConverter enemyConverterMock;
    private EnemiesWrapper enemiesWrapper;
    private TileConversionRule<MapTile> tileConverionRuleMock;

    @BeforeEach
    void init() {
        tileConverionRuleMock = mock(TileConversionRule.class);
        enemyConverterMock = mock(EnemyConverter.class);
        enemiesWrapper = new EnemiesWrapper();

        underTest = new MapConverter(enemyConverterMock, enemiesWrapper, tileConverionRuleMock);
    }

    @Test
    void convert() {
        when(tileConverionRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = new MapPrototype(
            "name",
            mockMapMatrix(1, 1),
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        Map result = underTest.convert(input);

        assertThat(result.sizeX(), is(1));
        assertThat(result.sizeY(), is(1));
    }

    @Test
    void convert3x2() {
        when(tileConverionRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = new MapPrototype(
            "name",
            mockMapMatrix(3, 2),
            List.of(),
            List.of(),
            List.of(),
            List.of()
        );

        Map result = underTest.convert(input);

        assertThat(result.sizeX(), is(3));
        assertThat(result.sizeY(), is(2));
    }

    @Test
    void triggersLoadEnemies() {
        EnemyFactory enemyFactoryMock = mock(EnemyFactory.class);
        when(enemyFactoryMock.createOnMap(any())).thenReturn(mock(Enemy.class));

        when(enemyConverterMock.convert(any())).thenReturn(enemyFactoryMock);
        when(tileConverionRuleMock.applicableTo(any(), any())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = new MapPrototype(
            "name",
            mockMapMatrix(1, 1),
            List.of(),
            List.of(),
            List.of(),
            List.of(new EnemyPrototype("name", PositiveInt.of(0), PositiveInt.of(0), null, PositiveInt.of(0), PositiveInt.of(0)))
        );

        underTest.convert(input);

        assertThat(enemiesWrapper.getEnemies(), hasSize(1));
    }

    private MapMatrix<TilePrototype> mockMapMatrix(int sizeX, int sizeY) {
        MapMatrix<TilePrototype> mapMatrixMock = mock(MapMatrix.class);
        when(mapMatrixMock.sizeX()).thenReturn(sizeX);
        when(mapMatrixMock.sizeY()).thenReturn(sizeY);
        return mapMatrixMock;
    }
}
