package manfred.game.map;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTest {
    @Test
    public void isAccessible() {
        Map unterTest = new Map(List.of(List.of(new NotAccessible(), Accessible.getInstance())));

        assertFalse(unterTest.isAccessible(0, 0));
        assertTrue(unterTest.isAccessible(0, 1));

        // test out of bounds
        assertFalse(unterTest.isAccessible(-1, 1));
        assertFalse(unterTest.isAccessible(0, -1));
        assertFalse(unterTest.isAccessible(55, 1));
        assertFalse(unterTest.isAccessible(0, 55));
    }
}
