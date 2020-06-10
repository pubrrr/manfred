package manfred.game.graphics;

import manfred.game.characters.Manfred;
import manfred.game.map.MapWrapper;

import java.awt.*;

public class BackgroundScroller {
    private final int triggerScrollDistanceToBorder;
    private final Manfred manfred;
    private final MapWrapper mapWrapper;

    private Point offset = new Point(0, 0);

    public BackgroundScroller(int triggerScrollDistanceToBorder, Manfred manfred, MapWrapper mapWrapper) {
        this.triggerScrollDistanceToBorder = triggerScrollDistanceToBorder;
        this.manfred = manfred;
        this.mapWrapper = mapWrapper;
    }

    public Point getOffset() {
        updateX();
        updateY();
        return offset;
    }

    private void updateX() {
        int mapSizeX = mapWrapper.getMap().getArray().length * GamePanel.PIXEL_BLOCK_SIZE;
        if (mapSizeX < GamePanel.WIDTH) {
            offset.x = - (GamePanel.WIDTH - mapSizeX) / 2;
            return;
        }

        int distanceToLeftBorder = manfred.getSprite().getLeft() - offset.x;
        int distanceToRightBorder = GamePanel.WIDTH - (manfred.getSprite().getRight() - offset.x);

        if (distanceToLeftBorder < triggerScrollDistanceToBorder) {
            offset.x = Math.max(
                    offset.x - (triggerScrollDistanceToBorder - distanceToLeftBorder),
                    0
            );
        } else if (distanceToRightBorder < triggerScrollDistanceToBorder) {
            offset.x = Math.min(
                    offset.x + (triggerScrollDistanceToBorder - distanceToRightBorder),
                    mapSizeX - GamePanel.WIDTH
            );
        }
    }

    private void updateY() {
        int mapSizeY = mapWrapper.getMap().getArray()[0].length * GamePanel.PIXEL_BLOCK_SIZE;
        if (mapSizeY < GamePanel.HEIGHT) {
            offset.y = - (GamePanel.HEIGHT - mapSizeY) / 2;
            return;
        }

        int distanceToTopBorder = manfred.getSprite().getTop() - offset.y;
        int distanceToBottomBorder = GamePanel.HEIGHT - (manfred.getSprite().getBottom() - offset.y);

        if (distanceToTopBorder < triggerScrollDistanceToBorder) {
            offset.y = Math.max(
                    offset.y - (triggerScrollDistanceToBorder - distanceToTopBorder),
                    0
            );
        } else if (distanceToBottomBorder < triggerScrollDistanceToBorder) {
            offset.y = Math.min(
                    offset.y + (triggerScrollDistanceToBorder - distanceToBottomBorder),
                    mapSizeY - GamePanel.HEIGHT
            );
        }
    }
}
