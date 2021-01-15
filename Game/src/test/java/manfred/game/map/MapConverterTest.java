package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.data.TextFileReader;
import manfred.data.enemy.EnemyDto;
import manfred.data.enemy.EnemyReader;
import manfred.data.image.ImageLoader;
import manfred.data.person.PersonReader;
import manfred.game.config.GameConfig;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.EnemyConverter;
import manfred.game.exception.ManfredException;
import manfred.game.interact.Door;
import manfred.game.interact.Portal;
import manfred.game.interact.person.PersonConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MapConverterTest {
    private MapConverter underTest;
    private EnemyConverter enemyConverterMock;
    private EnemiesWrapper enemiesWrapperMock;
    private ImageLoader imageLoaderMock;
    private PersonReader personReaderMock;
    private EnemyReader enemyReaderMock;

    @BeforeEach
    void init() {
        PersonConverter personConverterMock = mock(PersonConverter.class);
        enemyConverterMock = mock(EnemyConverter.class);
        enemiesWrapperMock = mock(EnemiesWrapper.class);
        imageLoaderMock = mock(ImageLoader.class);
        personReaderMock = mock(PersonReader.class);
        enemyReaderMock = mock(EnemyReader.class);

        underTest = new MapConverter(personConverterMock, enemyConverterMock, enemyReaderMock, enemiesWrapperMock, mock(GameConfig.class), imageLoaderMock, new TextFileReader(), personReaderMock);
    }

    @Test
    void convert() throws ManfredException, InvalidInputException {
        Map result = underTest.convert("{name: test, map: [[]]}");

        assertEquals("test", result.getName());
        assertArrayEquals(new String[0][1], result.getArray());
    }

    @Test
    void convertsMapWithString() throws ManfredException, InvalidInputException {
        String jsonWithStrings = "{name: test, map: [['1', '0']]}";

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertTrue(result.getArray()[0][0] instanceof Accessible);
        assertTrue(result.getArray()[1][0] instanceof NotAccessible);
    }

    @Test
    void convertsMapWithInt() throws ManfredException, InvalidInputException {
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
    void convertsMapWithStringAndInt() throws ManfredException, InvalidInputException {
        String jsonWithStrings = "{name: test ,map: [['0', 0], [1,1], ['1', 1]]}";

        Map result = underTest.convert(jsonWithStrings);

        assertEquals("test", result.getName());
        assertTrue(result.getArray()[1][0] instanceof NotAccessible);
        assertTrue(result.getArray()[0][1] instanceof Accessible);
    }

    @Test
    void readAndConvert() throws ManfredException, IOException, InvalidInputException {
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
    void triggersLoadPerson() throws IOException, ManfredException, InvalidInputException {
        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
        underTest.convert(json);

        verify(personReaderMock).load("opa");
    }

    @Test
    void loadsDoor() throws ManfredException, InvalidInputException {
        String jsonWithDoor = "{name: test, map: [[0, 0]], interactables: [{ type: Door, positionX: 1, positionY: 0, target: testTaraget, targetSpawnX: 1, targetSpawnY: 1}]}";
        Map result = underTest.convert(jsonWithDoor);

        assertTrue(result.getInteractable(new Point(1, 0)) instanceof Door);
    }

    @Test
    void loadsPortal() throws ManfredException, InvalidInputException {
        String jsonWithPortal = "{name: test, map: [[0, 0]], interactables: [{ type: Portal, positionX: 1, positionY: 0, target: testTaraget, targetSpawnX: 1, targetSpawnY: 1}]}";
        Map result = underTest.convert(jsonWithPortal);

        assertTrue(result.getInteractable(new Point(1, 0)) instanceof Portal);
    }

    @Test
    void triggersLoadEnemies() throws ManfredException, InvalidInputException {
        when(enemyReaderMock.load(any())).thenReturn(new EnemyDto());

        String jsonWithEnemy = "{name: test, map: [[0]], enemies: [{name: testEnemy, spawnX: 0, spawnY: 55}]}";
        underTest.convert(jsonWithEnemy);

        verify(enemyReaderMock).load("testEnemy");
        verify(enemyConverterMock).convert(any(), eq(0), eq(55));
        verify(enemiesWrapperMock).setEnemies(any());
    }

    @Test
    void triggerLoadTileImage() throws ManfredException, InvalidInputException {
        String input = "{name: test, map: [[tileName]]}";
        underTest.convert(input);

        verify(imageLoaderMock).load("ManfredsApocalypse\\Data\\data\\maps\\tiles\\tileName.png");
    }
}
