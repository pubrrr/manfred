package manfred.game.map;

import manfred.data.infrastructure.enemy.LocatedEnemyDto;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.game.config.GameConfig;
import manfred.game.conversion.map.TileConversionRule;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.Enemy;
import manfred.game.enemy.EnemyConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapConverterTest {
    private MapConverter underTest;
    private EnemyConverter enemyConverterMock;
    private EnemiesWrapper enemiesWrapper;
    private TileConversionRule tileConverionRuleMock;

    @BeforeEach
    void init() {
        enemyConverterMock = mock(EnemyConverter.class);
        enemiesWrapper = new EnemiesWrapper();
        tileConverionRuleMock = mock(TileConversionRule.class);

        underTest = new MapConverter(enemyConverterMock, enemiesWrapper, mock(GameConfig.class), tileConverionRuleMock);
    }

    @Test
    void convert() {
        when(tileConverionRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(NotAccessible::new));

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
        when(tileConverionRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(NotAccessible::new));

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
        when(enemyConverterMock.convert(any())).thenReturn(mock(Enemy.class));
        when(tileConverionRuleMock.applicableTo(any(), anyInt(), anyInt())).thenReturn(Optional.of(NotAccessible::new));

        MapPrototype input = new MapPrototype(
            "name",
            mockMapMatrix(1, 1),
            List.of(),
            List.of(),
            List.of(),
            List.of(new LocatedEnemyDto("name", 0, 0, null, 0, 0))
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
