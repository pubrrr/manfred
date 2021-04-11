package manfred.data.infrastructure.map.validator;

import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.reader.UrlHelper;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemyValidatorTest {

    private EnemyValidator underTest;
    private UrlHelper urlHelperMock;

    @BeforeEach
    void setUp() {
        urlHelperMock = mock(UrlHelper.class);
        underTest = new EnemyValidator(urlHelperMock);
    }

    @Test
    void noEnemies() {
        RawMapDto input = getRawMapWithEnemies();

        List<String> result = underTest.validate(input, accessibleMap());

        assertThat(result, empty());
    }

    @Test
    void accessibleMapIsValid() {
        when(urlHelperMock.getFileForEnemy(any())).thenReturn(new File(getClass().getResource("/existingFile").getFile()));

        RawMapDto input = getRawMapWithEnemies(new MapEnemyDto("target", PositiveInt.of(0), PositiveInt.of(0)));

        MapMatrix<TilePrototype> mapMatrixMock = accessibleMap();
        List<String> result = underTest.validate(input, mapMatrixMock);

        assertThat(result, empty());
    }

    @Test
    void nonAccessibleMapIsNotValid() {
        when(urlHelperMock.getFileForEnemy(any())).thenReturn(new File(getClass().getResource("/existingFile").getFile()));

        RawMapDto input = getRawMapWithEnemies(new MapEnemyDto("target", PositiveInt.of(0), PositiveInt.of(0)));

        MapMatrix<TilePrototype> mapMatrixMock = nonAccessibleMap();
        List<String> result = underTest.validate(input, mapMatrixMock);

        assertThat(result, hasSize(1));
        assertThat(result, contains("Tile for enemy target is not accessible"));
    }

    @Test
    void accessibleAndAccessibleTile() {
        when(urlHelperMock.getFileForEnemy(any())).thenReturn(new File(getClass().getResource("/existingFile").getFile()));

        RawMapDto input = getRawMapWithEnemies(
            new MapEnemyDto("target1", PositiveInt.of(0), PositiveInt.of(0)),
            new MapEnemyDto("target2", PositiveInt.of(0), PositiveInt.of(1))
        );

        MapMatrix<TilePrototype> mapMatrixMock = mockMapMatrix();
        when(mapMatrixMock.get(eq(0), eq(0))).thenReturn(TilePrototype.accessible());
        when(mapMatrixMock.get(eq(0), eq(1))).thenReturn(TilePrototype.notAccessible());

        List<String> result = underTest.validate(input, mapMatrixMock);

        assertThat(result, hasSize(1));
        assertThat(result, contains("Tile for enemy target2 is not accessible"));
    }

    @Test
    void unknownResourceForTargetIsNotValid() {
        when(urlHelperMock.getFileForEnemy(any())).thenReturn(new File("non/existent/file"));

        RawMapDto input = getRawMapWithEnemies(new MapEnemyDto("targetName", PositiveInt.of(0), PositiveInt.of(0)));

        List<String> result = underTest.validate(input, accessibleMap());

        assertThat(result, hasSize(1));
        assertThat(result, contains("Resource for enemy targetName not found"));
    }

    @Test
    void unknownResourceAndNonAccessibleMap() {
        when(urlHelperMock.getFileForEnemy(any())).thenReturn(new File("non/existent/file"));

        RawMapDto input = getRawMapWithEnemies(new MapEnemyDto("targetName", PositiveInt.of(0), PositiveInt.of(0)));

        List<String> result = underTest.validate(input, nonAccessibleMap());

        assertThat(result, hasSize(2));
        assertThat(
            result,
            containsInAnyOrder("Tile for enemy targetName is not accessible", "Resource for enemy targetName not found")
        );
    }

    private RawMapDto getRawMapWithEnemies(MapEnemyDto... enemies) {
        return new RawMapDto("name", List.of(), List.of(), List.of(), List.of(), Arrays.asList(enemies), null);
    }

    private MapMatrix<TilePrototype> accessibleMap() {
        MapMatrix<TilePrototype> mapMatrixMock = mockMapMatrix();
        when(mapMatrixMock.get(anyInt(), anyInt())).thenReturn(TilePrototype.accessible());
        return mapMatrixMock;
    }

    private MapMatrix<TilePrototype> nonAccessibleMap() {
        MapMatrix<TilePrototype> mapMatrixMock = mockMapMatrix();
        when(mapMatrixMock.get(anyInt(), anyInt())).thenReturn(TilePrototype.notAccessible());
        return mapMatrixMock;
    }

    @SuppressWarnings("unchecked")
    private MapMatrix<TilePrototype> mockMapMatrix() {
        return mock(MapMatrix.class);
    }
}