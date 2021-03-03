package manfred.data.infrastructure.map.tile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccessibilityTileConverterTest {

    private AccessibilityTileConverter underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccessibilityTileConverter();
    }

    @Test
    void notAccessible() {
        assertFalse(underTest.stringToObject("0").isAccessible());
        assertFalse(underTest.stringToObject("_").isAccessible());
        assertFalse(underTest.stringToObject("_startsWith_").isAccessible());
    }

    @Test
    void everythingElseIsAccessible() {
        assertTrue(underTest.stringToObject("1").isAccessible());
        assertTrue(underTest.stringToObject("a").isAccessible());
        assertTrue(underTest.stringToObject("3").isAccessible());
        assertTrue(underTest.stringToObject("5").isAccessible());
        assertTrue(underTest.stringToObject("asdlkfj").isAccessible());
        assertTrue(underTest.stringToObject("").isAccessible());
    }
}