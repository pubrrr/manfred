package helpers;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.config.GameConfig;

import java.util.Objects;

public class TestGameConfig extends GameConfig {
    public static final int STANDARD_TEST_WINDOW_WIDTH = 1600;
    public static final int STANDARD_TEST_WINDOW_HEIGTH = 1200;
    public static final int STANDARD_TEST_PIXEL_BLOCK_SIZE = 60;
    public static final int STANDARD_TEST_TEXT_BOX_DISTANCE_TO_BORDER = 100;
    public static final int STANDARD_TEST_TEXT_POINT_SIZE = 20;
    public static final int STANDARD_TEST_TEXT_DISTANCE_TO_BOX = 50;
    public static final int STANDARD_TEST_GELABER_BOX_POSITION_X = 100;
    public static final int STANDARD_TEST_GELABER_BOX_POSITION_Y = 100;

    private PositiveInt.Strict testPixelBlockSize = null;
    private Integer testNumberOfTextLines = null;
    private Integer testWindowHeight = null;
    private Integer testWindowWidth = null;
    private Integer charactersPerLine = null;

    public TestGameConfig() {
        super(
            PositiveInt.ofNonZero(STANDARD_TEST_WINDOW_WIDTH),
            PositiveInt.ofNonZero(STANDARD_TEST_WINDOW_HEIGTH),
            PositiveInt.ofNonZero(STANDARD_TEST_PIXEL_BLOCK_SIZE),
            PositiveInt.of(STANDARD_TEST_TEXT_BOX_DISTANCE_TO_BORDER),
            PositiveInt.of(STANDARD_TEST_TEXT_POINT_SIZE),
            PositiveInt.of(STANDARD_TEST_TEXT_DISTANCE_TO_BOX),
            PositiveInt.of(STANDARD_TEST_GELABER_BOX_POSITION_X),
            PositiveInt.of(STANDARD_TEST_GELABER_BOX_POSITION_Y),
            false
        );
    }

    public TestGameConfig withPixelBlockSize(int pixelBlockSize) {
        this.testPixelBlockSize = PositiveInt.ofNonZero(pixelBlockSize);
        return this;
    }

    @Override
    public PositiveInt.Strict getPixelBlockSize() {
        if (testPixelBlockSize == null) {
            return super.getPixelBlockSize();
        }
        return testPixelBlockSize;
    }

    public TestGameConfig withNumberOfTextLines(int testNumberOfTextLines) {
        this.testNumberOfTextLines = testNumberOfTextLines;
        return this;
    }

    @Override
    public int getNumberOfTextLines() {
        return Objects.requireNonNullElseGet(testNumberOfTextLines, super::getNumberOfTextLines);
    }

    public TestGameConfig setWindowHeight(int height) {
        this.testWindowHeight = height;
        return this;
    }

    @Override
    public int getWindowHeight() {
        if (testWindowHeight == null) {
            return super.getWindowHeight();
        }
        return testWindowHeight;
    }

    public TestGameConfig setWindowWidth(int width) {
        this.testWindowWidth = width;
        return this;
    }

    @Override
    public int getWindowWidth() {
        if (testWindowWidth == null) {
            return super.getWindowWidth();
        }
        return testWindowWidth;
    }

    public GameConfig withCharactersPerGelaberLine(int characterPerLine) {
        this.charactersPerLine = characterPerLine;
        return this;
    }

    @Override
    public int getCharactersPerGelaberLine() {
        return Objects.requireNonNullElseGet(this.charactersPerLine, super::getCharactersPerGelaberLine);
    }
}
