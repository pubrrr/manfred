package manfred.game.map;

import manfred.game.GameConfig;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.EnemyReader;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.ImageLoader;
import manfred.game.interact.Door;
import manfred.game.interact.PersonReader;
import manfred.game.interact.Portal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MapReaderTest {
    private MapReader underTest;
    private PersonReader personReaderMock;
    private EnemyReader enemyReaderMock;
    private EnemiesWrapper enemiesWrapperMock;
    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() {
        personReaderMock = mock(PersonReader.class);
        enemyReaderMock = mock(EnemyReader.class);
        enemiesWrapperMock = mock(EnemiesWrapper.class);
        imageLoaderMock = mock(ImageLoader.class);

        underTest = new MapReader(personReaderMock, enemyReaderMock, enemiesWrapperMock, mock(GameConfig.class), imageLoaderMock);
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

        Map result = underTest.convert(jsonWithIntMap);

        assertEquals("test", result.getName());
        MapTile[][] resultArray = result.getArray();
        assertTrue(resultArray[0][0] instanceof NotAccessible);
        assertTrue(resultArray[1][0] instanceof Accessible);
        assertTrue(resultArray[0][1] instanceof Accessible);
        assertTrue(resultArray[1][1] instanceof NotAccessible);
    }

    @Test
    void convertsMapWithStringAndInt() throws InvalidInputException {
        String jsonWithStrings = "{name: test ,map: [['0', 0], [1,1], ['1', 1]]}";

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertTrue(result.getArray()[1][0] instanceof NotAccessible);
        assertTrue(result.getArray()[0][1] instanceof Accessible);
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
        String jsonWithDoor = "{name: test, map: [[0, 0]], interactables: [{ type: Door, positionX: 1, positionY: 0, target: testTaraget, targetSpawnX: 1, targetSpawnY: 1}]}";
        Map result = underTest.convert(jsonWithDoor);

        assertTrue(result.getInteractable(1, 0) instanceof Door);
    }

    @Test
    void loadsPortal() throws InvalidInputException {
        String jsonWithPortal = "{name: test, map: [[0, 0]], interactables: [{ type: Portal, positionX: 1, positionY: 0, target: testTaraget, targetSpawnX: 1, targetSpawnY: 1}]}";
        Map result = underTest.convert(jsonWithPortal);

        assertTrue(result.getInteractable(1, 0) instanceof Portal);
    }

    @Test
    void triggersLoadEnemies() throws InvalidInputException, IOException {
        String jsonWithEnemy = "{name: test, map: [[0]], enemies: [{name: testEnemy, spawnX: 0, spawnY: 55}]}";
        underTest.convert(jsonWithEnemy);

        verify(enemyReaderMock).load("testEnemy", 0, 55);
        verify(enemiesWrapperMock).setEnemies(any());
    }

    @Test
    void triggerLoadTileImage() throws InvalidInputException, IOException {
        String input = "{name: test, map: [[tileName]]}";
        underTest.convert(input);

        verify(imageLoaderMock).load("data\\maps\\tiles\\tileName.png");
    }
}
