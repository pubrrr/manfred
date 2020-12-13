package manfred.game.map;

import manfred.game.GameConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class MapTest {
    @Test
    public void isAccessible() {
        MapTile[][] map = {{new NotAccessible(null, null, 1, 0), Accessible.getInstance()}};
        Map unterTest = new Map("test", map, mock(GameConfig.class));

        assertFalse(unterTest.isAccessible(0, 0));
        assertTrue(unterTest.isAccessible(0, 1));

        // test out of bounds
        assertFalse(unterTest.isAccessible(-1, 1));
        assertFalse(unterTest.isAccessible(0, -1));
        assertFalse(unterTest.isAccessible(55, 1));
        assertFalse(unterTest.isAccessible(0, 55));
    }
}
