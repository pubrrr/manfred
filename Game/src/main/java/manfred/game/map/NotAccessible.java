package manfred.game.map;

import manfred.game.config.GameConfig;

import java.awt.*;
import java.awt.image.BufferedImage;

public class NotAccessible implements MapTile {
    private final BufferedImage tileImage;
    private final GameConfig gameConfig;
    private final int blocksWidth;
    private final int yOffset;

    public NotAccessible(BufferedImage tileImage, GameConfig gameConfig, int blocksWidth, int yOffset) {
        this.tileImage = tileImage;
        this.gameConfig = gameConfig;
        this.blocksWidth = blocksWidth;
        this.yOffset = yOffset;
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public BufferedImage getImage() {
        return tileImage;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        if (tileImage != null) {
            int imageWidth = gameConfig.getPixelBlockSize() * this.blocksWidth;
            int imageHeight = tileImage.getHeight() * imageWidth / tileImage.getWidth();
            g.drawImage(
                tileImage,
                x - offset.x,
                (y + gameConfig.getPixelBlockSize()) + yOffset * gameConfig.getPixelBlockSize() - offset.y - imageHeight,
                imageWidth,
                imageHeight,
                null
            );
        }
    }
}
