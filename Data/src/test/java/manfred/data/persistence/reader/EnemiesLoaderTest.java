package manfred.data.persistence.reader;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.enemy.EnemiesLoader;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.persistence.dto.EnemyDto;
import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.shared.PositiveInt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnemiesLoaderTest {

    private EnemiesLoader underTest;

    private EnemyReader enemyReaderMock;

    @BeforeEach
    void setUp() {
        enemyReaderMock = mock(EnemyReader.class);
        underTest = new EnemiesLoader(enemyReaderMock);
    }

    @Test
    void emptyInput() throws InvalidInputException {
        List<EnemyPrototype> result = underTest.load(List.of());

        assertThat(result, empty());
    }

    @Test
    void oneValidInput() throws InvalidInputException {
        when(enemyReaderMock.load(anyString())).thenReturn(new EnemyDto("name", PositiveInt.of(0), PositiveInt.of(0), null));

        PositiveInt positionX = PositiveInt.of(5);
        PositiveInt positionY = PositiveInt.of(10);
        List<EnemyPrototype> result = underTest.load(List.of(new MapEnemyDto("test", positionX, positionY)));

        assertThat(result, hasSize(1));
        EnemyPrototype enemyPrototype = result.get(0);
        assertThat(enemyPrototype.getSpawnX(), is(positionX));
        assertThat(enemyPrototype.getSpawnY(), is(positionY));
    }

    @Test
    void twoValidInputs() throws InvalidInputException {
        when(enemyReaderMock.load(anyString())).thenReturn(new EnemyDto("name", PositiveInt.of(0), PositiveInt.of(0), null));

        List<EnemyPrototype> result = underTest.load(List.of(
            new MapEnemyDto("test", PositiveInt.of(1), PositiveInt.of(2)),
            new MapEnemyDto("test", PositiveInt.of(1), PositiveInt.of(2))
        ));

        assertThat(result, hasSize(2));
    }

    @Test
    void oneValidOneInvalidInput() throws InvalidInputException {
        when(enemyReaderMock.load(same("valid"))).thenReturn(new EnemyDto("valid", PositiveInt.of(0), PositiveInt.of(0), null));
        when(enemyReaderMock.load(same("invalid"))).thenThrow(new InvalidInputException("enemyReaderEnemyMessage"));

        List<MapEnemyDto> input = List.of(
            new MapEnemyDto("valid", PositiveInt.of(1), PositiveInt.of(2)),
            new MapEnemyDto("invalid", PositiveInt.of(1), PositiveInt.of(2))
        );

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.load(input));
        assertThat(exception.getMessage(), containsString("enemyReaderEnemyMessage"));
    }

    @Test
    void twoInvalidInputs() throws InvalidInputException {
        when(enemyReaderMock.load(same("invalid1"))).thenThrow(new InvalidInputException("invalid1"));
        when(enemyReaderMock.load(same("invalid2"))).thenThrow(new InvalidInputException("invalid2"));

        List<MapEnemyDto> input = List.of(
            new MapEnemyDto("invalid1", PositiveInt.of(1), PositiveInt.of(2)),
            new MapEnemyDto("invalid2", PositiveInt.of(1), PositiveInt.of(2))
        );

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.load(input));
        assertThat(exception.getMessage(), containsString("invalid1,\ninvalid2"));
    }
}