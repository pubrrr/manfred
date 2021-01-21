package manfred.data.enemy;

import manfred.data.InvalidInputException;
import manfred.data.map.MapEnemyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        List<LocatedEnemyDto> result = underTest.load(List.of());

        assertThat(result, empty());
    }

    @Test
    void oneValidInput() throws InvalidInputException {
        when(enemyReaderMock.load(any())).thenReturn(new EnemyDto("name", 0, 0, null));

        int positionX = 5;
        int positionY = 10;
        List<LocatedEnemyDto> result = underTest.load(List.of(new MapEnemyDto("test", positionX, positionY)));

        assertThat(result, hasSize(1));
        LocatedEnemyDto locatedEnemyDto = result.get(0);
        assertThat(locatedEnemyDto.getSpawnX(), is(positionX));
        assertThat(locatedEnemyDto.getSpawnY(), is(positionY));
    }

    @Test
    void twoValidInputs() throws InvalidInputException {
        when(enemyReaderMock.load(any())).thenReturn(new EnemyDto("name", 0, 0, null));

        List<LocatedEnemyDto> result = underTest.load(List.of(
            new MapEnemyDto("test", 1, 2),
            new MapEnemyDto("test", 1, 2)
        ));

        assertThat(result, hasSize(2));
    }

    @Test
    void oneValidOneInvalidInput() throws InvalidInputException {
        when(enemyReaderMock.load(same("valid"))).thenReturn(new EnemyDto("valid", 0, 0, null));
        when(enemyReaderMock.load(same("invalid"))).thenThrow(new InvalidInputException("enemyReaderEnemyMessage"));

        List<MapEnemyDto> input = List.of(
            new MapEnemyDto("valid", 1, 2),
            new MapEnemyDto("invalid", 1, 2)
        );

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.load(input));
        assertThat(exception.getMessage(), containsString("enemyReaderEnemyMessage"));
    }

    @Test
    void twoInvalidInputs() throws InvalidInputException {
        when(enemyReaderMock.load(same("invalid1"))).thenThrow(new InvalidInputException("invalid1"));
        when(enemyReaderMock.load(same("invalid2"))).thenThrow(new InvalidInputException("invalid2"));

        List<MapEnemyDto> input = List.of(
            new MapEnemyDto("invalid1", 1, 2),
            new MapEnemyDto("invalid2", 1, 2)
        );

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> underTest.load(input));
        assertThat(exception.getMessage(), containsString("invalid1,\ninvalid2"));
    }
}