package manfred.game.config;

public class GameConfig {
    private final int windowWidth;
    private final int windowHeight;
    private final int pixelBlockSize;
    private int textBoxDistanceToBorder;
    private int textPointSize;
    private int textDistanceToBox;
    private int gelaberBoxPositionX;
    private int gelaberBoxPositionY;

    public GameConfig(
            int windowWidth,
            int windowHeight,
            int pixelBlockSize,
            int textBoxDistanceToBorder,
            int textPointSize,
            int textDistanceToBox,
            int gelaberBoxPositionX,
            int gelaberBoxPositionY
    ) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.pixelBlockSize = pixelBlockSize;
        this.textBoxDistanceToBorder = textBoxDistanceToBorder;
        this.textPointSize = textPointSize;
        this.textDistanceToBox = textDistanceToBox;
        this.gelaberBoxPositionX = gelaberBoxPositionX;
        this.gelaberBoxPositionY = gelaberBoxPositionY;
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

    public int getSelectionMovementDistance() {
        return getTextPointSize() + getDistanceBetweenLines();
    }
}
