package manfred.game.map;

import lombok.AllArgsConstructor;
import manfred.game.config.GameConfig;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public class MapTileWithImageDecorator implements MapTile {

    private final MapTile baseMapTile;
    private final BufferedImage tileImage;
    private final int imageWidth;
    private final int imageHeight;
    private final GameConfig gameConfig; // TODO this should not need to be here

    @Override
    public boolean isAccessible() {
        return baseMapTile.isAccessible();
    }

    @Override
    public void paint(Graphics g, Integer x, Integer y) {
        g.drawImage(
            tileImage,
            x,
            (y + gameConfig.getPixelBlockSize().value()) - imageHeight,
            imageWidth,
            imageHeight,
            null
        );
    }
}
