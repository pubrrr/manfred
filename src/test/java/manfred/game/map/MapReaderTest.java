package manfred.game.map;

import manfred.game.enemy.EnemyReader;
import manfred.game.exception.InvalidInputException;
import manfred.game.interact.Door;
import manfred.game.interact.PersonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MapReaderTest {
    private MapReader underTest;
    private PersonReader personReaderMock;
    private EnemyReader enemyReaderMock;

    @BeforeEach
    void init() {
        personReaderMock = mock(PersonReader.class);
        enemyReaderMock = mock(EnemyReader.class);

        underTest = new MapReader(personReaderMock, enemyReaderMock);
    }

    @Test
    void convert() throws InvalidInputException {
        Map result = underTest.convert("{name: test, map: [[]]}");

        assertEquals("test", result.getName());
        assertArrayEquals(new String[0][1], result.getArray());
    }

    @Test
    void convertsMapWithString() throws InvalidInputException {
        String jsonWithStrings = "{name: test, map: [['1', '0']]}";

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertTrue(result.getArray()[0][0] instanceof Accessible);
        assertTrue(result.getArray()[1][0] instanceof NotAccessible);
    }

    @Test
    void convertsMapWithInt() throws InvalidInputException {
        String jsonWithIntMap = "{name : test, map :[[0, 1], [1, 0]]}";
        MapTile[][] expectedMap = {{NotAccessible.getInstance(), Accessible.getInstance()}, {Accessible.getInstance(), NotAccessible.getInstance()}};

        Map result = underTest.convert(jsonWithIntMap);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    void convertsMapWithStringAndInt() throws InvalidInputException {
        String jsonWithStrings = "{name: test ,map: [['0', 0], [1,1], ['1', 1]]}";
        MapTile[][] expectedMap = {
                {NotAccessible.getInstance(), Accessible.getInstance(), Accessible.getInstance()},
                {NotAccessible.getInstance(), Accessible.getInstance(), Accessible.getInstance()}
        };

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertArrayEquals(expectedMap, result.getArray());
    }

    @Test
    void readAndConvert() throws InvalidInputException, IOException {
        String[][] expectedMap = {{"opa", "1", "0"}, {"0", "1", "1"}};

        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
        Map result = underTest.convert(json);

        assertEquals("test", result.getName());
        assertSame(Accessible.getInstance(), result.getArray()[1][1]);
        verify(personReaderMock).load("opa");
    }

    @Test
    void expectsRectangularMap() {
        final String invalidJson = "{name: test, map: [[0, 1], [0]]}";

        assertThrows(InvalidInputException.class, () -> underTest.convert(invalidJson));
    }

    @Test
    void triggersLoadPerson() throws IOException, InvalidInputException {
        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
        underTest.convert(json);

        verify(personReaderMock).load("opa");
    }

    @Test
    void loadsDoor() throws InvalidInputException {
        String jsonWithDoor = "{name: test, map: [[door]], interactables: {door :{ type: Door, targetSpawnX: 1, targetSpawnY: 1}}}";
        Map result = underTest.convert(jsonWithDoor);

        assertTrue(result.getInteractable(0, 0) instanceof Door);
    }

    @Test
    void triggersLoadEnemies() throws InvalidInputException, IOException {
        String jsonWithEnemy = "{name: test, map: [[0]], enemies: [{name: testEnemy, spawnX: 0, spawnY: 55}]}";
        underTest.convert(jsonWithEnemy);

        verify(enemyReaderMock).load("testEnemy", 0, 55);
    }
}
