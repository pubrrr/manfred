package manfred.game.graphics.scrolling;

import helpers.TestGameConfig;
import manfred.game.geometry.Vector;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.MapFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BackgroundScrollerTest {
    private final static int PIXEL_BLOCK_SIZE = 10;
    public static final int TRIGGER_SCROLL_DISTANCE_TO_BORDER = 10;

    private BackgroundScroller.Factory underTestFactory;

    private TestGameConfig testGameConfig;

    private MapFacade mapFacadeMock;

    @BeforeEach
    void init() {
        mapFacadeMock = mock(MapFacade.class);
        testGameConfig = (new TestGameConfig()).withPixelBlockSize(PIXEL_BLOCK_SIZE);

        underTestFactory = BackgroundScroller.factoryWith(TRIGGER_SCROLL_DISTANCE_TO_BORDER, mapFacadeMock, testGameConfig);
    }

    @Test
    void givenMapSmallerThanScreen_thenCentersMap() {
        setupMapWithDimensions(3, 3);
        testGameConfig.setWindowWidth(6 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(5 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(manfredAt(1, 1));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(1, 1));

        assertEquals(3 * PIXEL_BLOCK_SIZE / 2, result.x());
        assertEquals(-PIXEL_BLOCK_SIZE, result.y());
    }

    @Test
    void givenManfredNotCloseToBorder_thenDoesNotScroll() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(2 * PIXEL_BLOCK_SIZE, 2 * PIXEL_BLOCK_SIZE));

        assertEquals(0, result.x());
        assertEquals(0, result.y());
    }

    @Test
    void givenManfredCloseToRightBorder_thenScrollsRight() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(2 * PIXEL_BLOCK_SIZE + 1, 2 * PIXEL_BLOCK_SIZE));

        assertEquals(-1, result.x());
        assertEquals(0, result.y());
    }

    @Test
    void givenManfredCloseToBottomBorder_thenScrollsDown() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(2 * PIXEL_BLOCK_SIZE, 2 * PIXEL_BLOCK_SIZE + 2));

        assertEquals(0, result.x());
        assertEquals(2, result.y());
    }

    @Test
    void givenManfredCloseToBottomRightBorder_thenScrollsDiagonally() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(3 * PIXEL_BLOCK_SIZE - 1, 3 * PIXEL_BLOCK_SIZE - 1));

        assertEquals(-(PIXEL_BLOCK_SIZE - 1), result.x());
        assertEquals(PIXEL_BLOCK_SIZE - 1, result.y());
    }

    @Test
    void doesNotScrollFurtherIfMapEnds() {
        setupMapWithDimensions(3, 3);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(3 * PIXEL_BLOCK_SIZE - 1, 3 * PIXEL_BLOCK_SIZE - 1));

        assertEquals(0, result.x());
        assertEquals(0, result.y());
    }

    @Test
    void givenManfredTopLeft_thenDoesNotScroll() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(0, 0));

        assertEquals(0, result.x());
        assertEquals(0, result.y());
    }

    @Test
    void givenManfredCloseToLeftBorderAfterScrollingRight_thenScrollsLeft() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(3 * PIXEL_BLOCK_SIZE, 0));

        assertEquals(-TRIGGER_SCROLL_DISTANCE_TO_BORDER, result.x());
        assertEquals(0, result.y());

        Vector<PanelCoordinate> result2 = underTest.getOffset(manfredAt(PIXEL_BLOCK_SIZE * 3 / 2, 0));

        assertEquals(-TRIGGER_SCROLL_DISTANCE_TO_BORDER / 2, result2.x());
        assertEquals(0, result2.y());
    }

    @Test
    void givenManfredCloseToTopBorderAfterScrollingDown_thenScrollsUp() {
        setupMapWithDimensions(6, 6);
        testGameConfig.setWindowWidth(3 * PIXEL_BLOCK_SIZE);
        testGameConfig.setWindowHeight(3 * PIXEL_BLOCK_SIZE);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(new PanelCoordinate(0, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(0, 3 * PIXEL_BLOCK_SIZE));

        assertEquals(0, result.x());
        assertEquals(TRIGGER_SCROLL_DISTANCE_TO_BORDER, result.y());

        Vector<PanelCoordinate> result2 = underTest.getOffset(manfredAt(0, PIXEL_BLOCK_SIZE * 3 / 2));

        assertEquals(0, result2.x());
        assertEquals(TRIGGER_SCROLL_DISTANCE_TO_BORDER / 2, result2.y());
    }

    @Test
    void givenManfredInMiddleOfMap_thenCentersToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(manfredAt(initialPosition, initialPosition));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(initialPosition, initialPosition));

        assertEquals(-(initialPosition - screenSize / 2), result.x());
        assertEquals(initialPosition - screenSize / 2, result.y());
    }

    @Test
    void givenManfredMidLeft_thenCentersVerticallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(manfredAt(0, initialPosition));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(0, initialPosition));

        assertEquals(0, result.x());
        assertEquals(initialPosition - screenSize / 2, result.y());
    }

    @Test
    void givenManfredMidRight_thenCentersVerticallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(manfredAt(8 * PIXEL_BLOCK_SIZE - 1, initialPosition));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(8 * PIXEL_BLOCK_SIZE - 1, initialPosition));

        assertEquals(-(8 * PIXEL_BLOCK_SIZE - screenSize), result.x());
        assertEquals(initialPosition - screenSize / 2, result.y());
    }

    @Test
    void givenManfredMidTop_thenCentersHorizontallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(manfredAt(initialPosition, 0));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(initialPosition, 0));

        assertEquals(-(initialPosition - screenSize / 2), result.x());
        assertEquals(0, result.y());
    }

    @Test
    void givenManfredMidBottom_thenCentersHorizontallyToIt() {
        int initialPosition = 4 * PIXEL_BLOCK_SIZE;
        int screenSize = 3 * PIXEL_BLOCK_SIZE;

        setupMapWithDimensions(8, 8);
        testGameConfig.setWindowWidth(screenSize);
        testGameConfig.setWindowHeight(screenSize);

        BackgroundScroller underTest = underTestFactory.buildCenteredAt(manfredAt(initialPosition, 8 * PIXEL_BLOCK_SIZE - 1));
        Vector<PanelCoordinate> result = underTest.getOffset(manfredAt(initialPosition, 8 * PIXEL_BLOCK_SIZE - 1));

        assertEquals(-(initialPosition - screenSize / 2), result.x());
        assertEquals(8 * PIXEL_BLOCK_SIZE - screenSize, result.y());
    }

    private PanelCoordinate manfredAt(int x, int y) {
        return new PanelCoordinate(x, y);
    }

    private void setupMapWithDimensions(int x, int y) {
        when(mapFacadeMock.getMapSizeX()).thenReturn(x);
        when(mapFacadeMock.getMapSizeY()).thenReturn(y);
    }
}