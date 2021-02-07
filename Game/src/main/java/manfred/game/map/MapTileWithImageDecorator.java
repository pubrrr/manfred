package manfred.game.map;

import lombok.AllArgsConstructor;
import manfred.game.config.GameConfig;
import manfred.game.graphics.PanelCoordinate;

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
    public void paint(Graphics g, PanelCoordinate coordinate) {
        g.drawImage(
            tileImage,
            coordinate.getX(),
            (coordinate.getY() + gameConfig.getPixelBlockSize().value()) - imageHeight,
            imageWidth,
            imageHeight,
            null
        );
    }
}
