package manfred.game;

public class GameConfig {
    private final int windowWidth;
    private final int windowHeight;
    private final int pixelBlockSize;

    public GameConfig(int windowWidth, int windowHeight, int pixelBlockSize) {
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.pixelBlockSize = pixelBlockSize;
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
}
