package manfred.game.map;

import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class NotAccessible implements MapTile {
    private BufferedImage tileImage;
    private GameConfig gameConfig;
    private int blocksWidth;
    private int yOffset;

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
    @Nullable
    public Consumer<KeyControls> onStep() {
        return null;
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
