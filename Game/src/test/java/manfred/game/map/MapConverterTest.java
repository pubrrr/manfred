package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.data.enemy.EnemyReader;
import manfred.data.enemy.EnemyDto;
import manfred.data.image.ImageLoader;
import manfred.data.person.PersonReader;
import manfred.game.config.GameConfig;
import manfred.game.conversion.map.TileConversionRule;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.enemy.EnemyConverter;
import manfred.game.exception.ManfredException;
import manfred.game.interact.Door;
import manfred.game.interact.Portal;
import manfred.game.interact.person.PersonProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        PersonProvider personConverterMock = mock(PersonProvider.class);
        enemyConverterMock = mock(EnemyConverter.class);
        enemiesWrapperMock = mock(EnemiesWrapper.class);
        imageLoaderMock = mock(ImageLoader.class);
        personReaderMock = mock(PersonReader.class);
        enemyReaderMock = mock(EnemyReader.class);

        underTest = new MapConverter(personConverterMock, enemyConverterMock, enemiesWrapperMock, mock(GameConfig.class), mock(TileConversionRule.class));
    }

    @Test
    void convert() throws ManfredException, InvalidInputException {
        Map result = underTest.convert("{name: test, map: [[]]}");

        assertThat(result.sizeX(), is(0));
        assertThat(result.sizeY(), is(0));
    }

    @Test
    void convertsMapWithString() throws ManfredException, InvalidInputException {
        String jsonWithStrings = "{name: test, map: [['1', '0']]}";

        Map result = underTest.convert(jsonWithStrings);

        assertTrue(result.isAccessible(0, 0));
        assertFalse(result.isAccessible(1, 0));
    }

    @Test
    void convertsMapWithInt() throws ManfredException, InvalidInputException {
        String jsonWithIntMap = "{name : test, map :[[0, 1], [1, 0]]}";

        Map result = underTest.convert(jsonWithIntMap);

        assertFalse(result.isAccessible(0, 0));
        assertTrue(result.isAccessible(1, 0));
        assertTrue(result.isAccessible(0, 1));
        assertFalse(result.isAccessible(1, 1));
    }

    @Test
    void convertsMapWithStringAndInt() throws ManfredException, InvalidInputException {
        String jsonWithStrings = "{name: test ,map: [['0', 0], [1,1], ['1', 1]]}";

        Map result = underTest.convert(jsonWithStrings);

        assertFalse(result.isAccessible(1, 0));
        assertTrue(result.isAccessible(0, 1));
    }

//    @Test
//    void triggersLoadPerson() throws IOException, ManfredException, InvalidInputException {
//        String json = underTest.read("src\\test\\java\\manfred\\game\\map\\test.json");
//        underTest.convert(json);
//
//        verify(personReaderMock).load("opa");
//    }

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
//        verify(enemyConverterMock).convert(any(), eq(0), eq(55));
        verify(enemiesWrapperMock).setEnemies(any());
    }

    @Test
    void triggerLoadTileImage() throws ManfredException, InvalidInputException {
        String input = "{name: test, map: [[tileName]]}";
        underTest.convert(input);

        verify(imageLoaderMock).load("ManfredsApocalypse\\Data\\data\\maps\\tiles\\tileName.png");
    }
}
