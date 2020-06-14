package helpers;

import manfred.game.GameConfig;

public class TestGameConfig extends GameConfig {
    public static final int STANDARD_TEST_WINDOW_WIDTH = 1600;
    public static final int STANDARD_TEST_WINDOW_HEIGTH = 1200;
    public static final int STANDARD_TEST_PIXEL_BLOCK_SIZE = 60;
    public static final int STANDARD_TEST_TEXT_BOX_DISTANCE_TO_BORDER = 100;
    public static final int STANDARD_TEST_TEXT_POINT_SIZE = 20;
    public static final int STANDARD_TEST_TEXT_DISTANCE_TO_BOX = 50;
    public static final int STANDARD_TEST_GELABER_BOX_POSITION_X = 100;
    public static final int STANDARD_TEST_GELABER_BOX_POSITION_Y = 100;

    private Integer testPixelBlockSize = null;
    private Integer numberOfTextLines = null;

    public TestGameConfig() {
        super(
                STANDARD_TEST_WINDOW_WIDTH,
                STANDARD_TEST_WINDOW_HEIGTH,
                STANDARD_TEST_PIXEL_BLOCK_SIZE,
                STANDARD_TEST_TEXT_BOX_DISTANCE_TO_BORDER,
                STANDARD_TEST_TEXT_POINT_SIZE,
                STANDARD_TEST_TEXT_DISTANCE_TO_BOX,
                STANDARD_TEST_GELABER_BOX_POSITION_X,
                STANDARD_TEST_GELABER_BOX_POSITION_Y
        );
    }

    public TestGameConfig withPixelBlockSize(int pixelBlockSize) {
        this.testPixelBlockSize = pixelBlockSize;
        return this;
    }

    @Override
    public int getPixelBlockSize() {
        if (testPixelBlockSize == null) {
            return super.getPixelBlockSize();
        }
        return testPixelBlockSize;
    }

    public TestGameConfig withNumberOfTextLines(int numberOfTextLines) {
        this.numberOfTextLines = numberOfTextLines;
        return this;
    }

    @Override
    public int getNumberOfTextLines() {
        if (numberOfTextLines == null) {
            return super.getNumberOfTextLines();
        }
        return numberOfTextLines;
    }
}
