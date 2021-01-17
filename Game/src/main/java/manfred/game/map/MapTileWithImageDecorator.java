package manfred.game.map;

import lombok.AllArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

@AllArgsConstructor
public class MapTileWithImageDecorator implements MapTile {

    private final MapTile baseMapTile;
    private final BufferedImage tileImage;

    @Override
    public boolean isAccessible() {
        return baseMapTile.isAccessible();
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
//        int imageWidth = gameConfig.getPixelBlockSize() * this.blocksWidth;
//        int imageHeight = tileImage.getHeight() * imageWidth / tileImage.getWidth();
//        g.drawImage(
//            tileImage,
//            x - offset.x,
//            (y + gameConfig.getPixelBlockSize()) + yOffset * gameConfig.getPixelBlockSize() - offset.y - imageHeight,
//            imageWidth,
//            imageHeight,
//            null
//        );
    }
}
