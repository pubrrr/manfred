package manfred.game.graphics;

import helpers.TestGameConfig;
import helpers.TestMapFactory;
import manfred.game.characters.Manfred;
import manfred.game.characters.Sprite;
import manfred.game.map.Map;
import manfred.game.map.MapFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BackgroundScrollerTest {
    private final static int PIXEL_BLOCK_SIZE = 10;
    public static final int TRIGGER_SCROLL_DISTANCE_TO_BORDER = 10;

    private BackgroundScroller underTest;

    private Manfred manfredMock;
    private Map map;
    private TestGameConfig testGameConfig;

    private Sprite manfredPosition;

    @BeforeEach
    void init() {
        manfredPosition = new Sprite(0, 0, 0, 0, 0, null);

        manfredMock = mock(Manfred.class);
        when(manfredMock.getSprite()).thenAnswer(invocationOnMock -> manfredPosition);

        MapFacade mapFacadeMock = mock(MapFacade.class);
        when(mapFacadeMock.getMap()).thenAnswer(invocationOnMock -> map);

        testGameConfig = (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE);

        underTest = new BackgroundScroller(TRIGGER_SCROLL_DISTANCE_TO_BORDER, manfredMock, mapFacadeMock, testGameConfig);
    }

    @Test
    void givenMapSmallerThanScreen_thenCentersMap() {
        setupMapWithDimensions(3, 3);
        testGameConfig.setWindowWidth(6 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(5 * PIXEL_BLOCK_SIZE);

        Point result = underTest.getOffset();

        assertEquals(-3 * PIXEL_BLOCK_SIZE / 2, result.x);
        assertEquals(-PIXEL_BLOCK_SIZE, result.y);
    }

    @Test
    void givenManfredNotCloseToBorder_thenDoesNotScroll() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 2 * PIXEL_BLOCK_SIZE;
        manfredPosition.y = 2 * PIXEL_BLOCK_SIZE;

        Point result = underTest.getOffset();

        assertEquals(0, result.x);
        assertEquals(0, result.y);
    }

    @Test
    void givenManfredCloseToRightBorder_thenScrollsRight() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 2 * PIXEL_BLOCK_SIZE + 1;
        manfredPosition.y = 2 * PIXEL_BLOCK_SIZE;

        Point result = underTest.getOffset();

        assertEquals(1, result.x);
        assertEquals(0, result.y);
    }

    @Test
    void givenManfredCloseToBottomBorder_thenScrollsDown() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 2 * PIXEL_BLOCK_SIZE;
        manfredPosition.y = 2 * PIXEL_BLOCK_SIZE + 2;

        Point result = underTest.getOffset();

        assertEquals(0, result.x);
        assertEquals(2, result.y);
    }

    @Test
    void givenManfredCloseToBottomRightBorder_thenScrollsDiagonally() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 3 * PIXEL_BLOCK_SIZE - 1;
        manfredPosition.y = 3 * PIXEL_BLOCK_SIZE - 1;

        Point result = underTest.getOffset();

        assertEquals(PIXEL_BLOCK_SIZE - 1, result.x);
        assertEquals(PIXEL_BLOCK_SIZE - 1, result.y);
    }

    @Test
    void doesNotScrollFurtherIfMapEnds() {
        setupMapWithDimensions(3, 3);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 3 * PIXEL_BLOCK_SIZE - 1;
        manfredPosition.y = 3 * PIXEL_BLOCK_SIZE - 1;

        Point result = underTest.getOffset();

        assertEquals(0, result.x);
        assertEquals(0, result.y);
    }

    @Test
    void givenManfredTopLeft_thenDoesNotScroll() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 0;
        manfredPosition.y = 0;

        Point result = underTest.getOffset();

        assertEquals(0, result.x);
        assertEquals(0, result.y);
    }

    @Test
    void givenManfredCloseToLeftBorderAfterScrollingRight_thenScrollsLeft() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 3 * PIXEL_BLOCK_SIZE;
        manfredPosition.y = 0;

        Point result = underTest.getOffset();

        assertEquals(TRIGGER_SCROLL_DISTANCE_TO_BORDER, result.x);
        assertEquals(0, result.y);

        manfredPosition.x = PIXEL_BLOCK_SIZE * 3 / 2;

        Point result2 = underTest.getOffset();

        assertEquals(TRIGGER_SCROLL_DISTANCE_TO_BORDER / 2, result2.x);
        assertEquals(0, result2.y);
    }

    @Test
    void givenManfredCloseToTopBorderAfterScrollingDown_thenScrollsUp() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);
        manfredPosition.x = 0;
        manfredPosition.y = 3 * PIXEL_BLOCK_SIZE;

        Point result = underTest.getOffset();

        assertEquals(0, result.x);
        assertEquals(TRIGGER_SCROLL_DISTANCE_TO_BORDER, result.y);

        manfredPosition.y = PIXEL_BLOCK_SIZE * 3 / 2;

        Point result2 = underTest.getOffset();

        assertEquals(0, result2.x);
        assertEquals(TRIGGER_SCROLL_DISTANCE_TO_BORDER / 2, result2.y);
    }

    @Test
    void givenManfredInMiddleOfMap_thenCentersToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);
        manfredPosition.x = initialPosition;
        manfredPosition.y = initialPosition;

        underTest.centerTo(manfredPosition.getCenter());
        Point result = underTest.getOffset();

        assertEquals(initialPosition - screenSize / 2, result.x);
        assertEquals(initialPosition - screenSize / 2, result.y);
    }

    @Test
    void givenManfredMidLeft_thenCentersVerticallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);
        manfredPosition.x = 0;
        manfredPosition.y = initialPosition;

        underTest.centerTo(manfredPosition.getCenter());
        Point result = underTest.getOffset();

        assertEquals(0, result.x);
        assertEquals(initialPosition - screenSize / 2, result.y);
    }

    @Test
    void givenManfredMidRight_thenCentersVerticallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);
        manfredPosition.x = 8 * PIXEL_BLOCK_SIZE - 1;
        manfredPosition.y = initialPosition;

        underTest.centerTo(manfredPosition.getCenter());
        Point result = underTest.getOffset();

        assertEquals(8 * PIXEL_BLOCK_SIZE - screenSize, result.x);
        assertEquals(initialPosition - screenSize / 2, result.y);
    }

    @Test
    void givenManfredMidTop_thenCentersHorizontallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);
        manfredPosition.x = initialPosition;
        manfredPosition.y = 0;

        underTest.centerTo(manfredPosition.getCenter());
        Point result = underTest.getOffset();

        assertEquals(initialPosition - screenSize / 2, result.x);
        assertEquals(0, result.y);
    }

    @Test
    void givenManfredMidBottom_thenCentersHorizontallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);
        manfredPosition.x = initialPosition;
        manfredPosition.y = 8 * PIXEL_BLOCK_SIZE - 1;

        underTest.centerTo(manfredPosition.getCenter());
        Point result = underTest.getOffset();

        assertEquals(initialPosition - screenSize / 2, result.x);
        assertEquals(8 * PIXEL_BLOCK_SIZE - screenSize, result.y);
    }

    private void setupMapWithDimensions(int x, int y) {
        // lazy way to initialize a String[][] with "0" values
        String[][] mapAsString = Arrays.stream(new int[x][y]).map(
                a -> Arrays.stream(a).mapToObj(String::valueOf).toArray(String[]::new)
        ).toArray(String[][]::new);
        this.map = TestMapFactory.create(mapAsString, null);
    }
}