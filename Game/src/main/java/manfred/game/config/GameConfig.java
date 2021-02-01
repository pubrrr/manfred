package manfred.game.config;

import manfred.data.shared.PositiveInt;

public class GameConfig {
    private final int windowWidth;
    private final int windowHeight;
    private final int pixelBlockSize;
    private final int textBoxDistanceToBorder;
    private final int textPointSize;
    private final int textDistanceToBox;
    private final int gelaberBoxPositionX;
    private final int gelaberBoxPositionY;

    public GameConfig(
        PositiveInt windowWidth,
        PositiveInt windowHeight,
        PositiveInt pixelBlockSize,
        PositiveInt textBoxDistanceToBorder,
        PositiveInt textPointSize,
        PositiveInt textDistanceToBox,
        PositiveInt gelaberBoxPositionX,
        PositiveInt gelaberBoxPositionY
    ) {
        this.windowWidth = windowWidth.value();
        this.windowHeight = windowHeight.value();
        this.pixelBlockSize = pixelBlockSize.value();
        this.textBoxDistanceToBorder = textBoxDistanceToBorder.value();
        this.textPointSize = textPointSize.value();
        this.textDistanceToBox = textDistanceToBox.value();
        this.gelaberBoxPositionX = gelaberBoxPositionX.value();
        this.gelaberBoxPositionY = gelaberBoxPositionY.value();
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public int getPixelBlockSize() {
        return pixelBlockSize;
    }

    public int getTextPointSize() {
        return textPointSize;
    }

    public int getTextBoxDistanceToBorder() {
        return textBoxDistanceToBorder;
    }

    public int getTextDistanceToBox() {
        return textDistanceToBox;
    }

    public int getTextBoxPositionX() {
        return textBoxDistanceToBorder;
    }

    public int getGelaberBoxPositionX() {
        return gelaberBoxPositionX;
    }

    public int getGelaberBoxPositionY() {
        return gelaberBoxPositionY;
    }

    public int getTextBoxPositionY() {
        return windowHeight * 2 / 3;
    }

    public int getTextBoxWidth() {
        return windowWidth - 2 * textBoxDistanceToBorder;
    }

    public int getTextBoxHeight() {
        return windowHeight / 3 - textBoxDistanceToBorder;
    }

    public int getCharactersPerGelaberLine() {
        return (getTextBoxWidth() - 2 * textDistanceToBox) / (textPointSize / 2);
    }

    public int getDistanceBetweenLines() {
        return textPointSize * 2 / 3;
    }

    public int getNumberOfTextLines() {
        return (getTextBoxHeight() - 2 * textDistanceToBox) / (textPointSize + getDistanceBetweenLines());
    }
}
