package manfred.game.enemy;

import helpers.TestGameConfig;
import manfred.game.characters.MapCollider;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.ImageLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EnemyReaderTest {
    private static final int PIXEL_BLOCK_SIZE = 40;

    private EnemyReader underTest;
    private ImageLoader imageLoaderMock;

    @BeforeEach
    void init() throws Exception {
        MapCollider mapColliderMock = mock(MapCollider.class);
        when(mapColliderMock.collides(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        MapColliderProvider mapColliderProviderMock = mock(MapColliderProvider.class);
        when(mapColliderProviderMock.provide()).thenReturn(mapColliderMock);

        imageLoaderMock = mock(ImageLoader.class);

        underTest = new EnemyReader(mapColliderProviderMock, imageLoaderMock, (new TestGameConfig()).setPixelBlockSize(PIXEL_BLOCK_SIZE));
    }

    @Test
    void testConvert() throws InvalidInputException, IOException {
        String input = "{name: testName, healthPoints: 100, speed: 1}";

        Enemy result = underTest.convert(input, 1, 22);

        verify(imageLoaderMock).load("ManfredsApocalypse\\data\\enemies\\testName.png");
        assertEquals(PIXEL_BLOCK_SIZE, result.getX());
        assertEquals(PIXEL_BLOCK_SIZE * 22, result.getY());
        assertEquals(100, result.getHealthPoints());

        result.right();
        result.move();
        assertEquals(PIXEL_BLOCK_SIZE + 1, result.getX());
    }
}