package manfred.game.attack;

import manfred.data.InvalidInputException;
import manfred.data.TextFileReader;
import manfred.data.image.ImageLoader;
import manfred.game.characters.Direction;
import manfred.game.characters.MapCollider;
import manfred.game.characters.Sprite;
import manfred.game.enemy.Enemy;
import manfred.game.enemy.MapColliderProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttackReaderTest {
    private AttackReader underTest;

    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() throws Exception {
        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mock(MapCollider.class));

        imageLoaderMock = mock(ImageLoader.class);

        underTest = new AttackReader(mapColliderProviderMock, imageLoaderMock, new TextFileReader());
    }

    @Test
    void convertSpeed() throws InvalidInputException {
        String input = "{name: testName, speed: 1, sizeX: 0, sizeY: 0, damage: 100, range: 2, numberOfAnimationImages: 3}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.RIGHT);
        attack.move();
        assertEquals(1, attack.getX());
        assertEquals(0, attack.getY());
    }

    @Test
    void convertDamage() throws InvalidInputException {
        String input = "{name: testName, speed: 1, sizeX: 0, sizeY: 0, damage: 100, range: 2, numberOfAnimationImages: 3}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.RIGHT);

        Sprite enemySpriteMock = mock(Sprite.class);
        when(enemySpriteMock.intersects(any())).thenReturn(true);
        Enemy enemyMock = mock(Enemy.class);
        when(enemyMock.getSprite()).thenReturn(enemySpriteMock);

        attack.checkHit(enemyMock);
        verify(enemyMock).takeDamage(100);
    }

    @Test
    void convertRange() throws InvalidInputException {
        String input = "{name: testName, speed: 2, sizeX: 0, sizeY: 0, damage: 100, range: 2, numberOfAnimationImages: 3}";

        AttackGenerator result = underTest.convert(input);

        Attack attack = result.generate(new Point(0,0), Direction.RIGHT);

        assertFalse(attack.isResolved());
        attack.move();
        assertTrue(attack.isResolved());
    }

    @Test
    void triggersLoadAnimation() throws InvalidInputException {
        String input = "{name: testName, speed: 1, sizeX: 0, sizeY: 0, damage: 100, range: 2, numberOfAnimationImages: 3}";

        underTest.convert(input);

        verify(imageLoaderMock, times(3)).load(anyString());
    }
}