package manfred.game.graphics;

import manfred.game.config.GameConfig;
import manfred.game.characters.Manfred;
import manfred.game.map.MapWrapper;

import java.awt.*;

public class BackgroundScroller {
    private final int triggerScrollDistanceToBorder;
    private final Manfred manfred;
    private final MapWrapper mapWrapper;
    private GameConfig gameConfig;

    private Point offset = new Point(0, 0);

    public BackgroundScroller(int triggerScrollDistanceToBorder, Manfred manfred, MapWrapper mapWrapper, GameConfig gameConfig) {
        this.triggerScrollDistanceToBorder = triggerScrollDistanceToBorder;
        this.manfred = manfred;
        this.mapWrapper = mapWrapper;
        this.gameConfig = gameConfig;
    }

    public Point getOffset() {
        updateX();
        updateY();
        return offset;
    }

    private void updateX() {
        int mapSizeX = mapWrapper.getMap().getArray().length * gameConfig.getPixelBlockSize();
        if (mapSizeX < gameConfig.getWindowWidth()) {
            offset.x = -(gameConfig.getWindowWidth() - mapSizeX) / 2;
            return;
        }

        int distanceToLeftBorder = manfred.getSprite().getLeft() - offset.x;
        int distanceToRightBorder = gameConfig.getWindowWidth() - (manfred.getSprite().getRight() - offset.x);

        if (distanceToLeftBorder < triggerScrollDistanceToBorder) {
            offset.x = Math.max(
                    offset.x - (triggerScrollDistanceToBorder - distanceToLeftBorder),
                    0
            );
        } else if (distanceToRightBorder < triggerScrollDistanceToBorder) {
            offset.x = Math.min(
                    offset.x + (triggerScrollDistanceToBorder - distanceToRightBorder),
                    mapSizeX - gameConfig.getWindowWidth()
            );
        }
    }

    private void updateY() {
        int mapSizeY = mapWrapper.getMap().getArray()[0].length * gameConfig.getPixelBlockSize();
        if (mapSizeY < gameConfig.getWindowHeight()) {
            offset.y = -(gameConfig.getWindowHeight() - mapSizeY) / 2;
            return;
        }

        int distanceToTopBorder = manfred.getSprite().getTop() - offset.y;
        int distanceToBottomBorder = gameConfig.getWindowHeight() - (manfred.getSprite().getBottom() - offset.y);

        if (distanceToTopBorder < triggerScrollDistanceToBorder) {
            offset.y = Math.max(
                    offset.y - (triggerScrollDistanceToBorder - distanceToTopBorder),
                    0
            );
        } else if (distanceToBottomBorder < triggerScrollDistanceToBorder) {
            offset.y = Math.min(
                    offset.y + (triggerScrollDistanceToBorder - distanceToBottomBorder),
                    mapSizeY - gameConfig.getWindowHeight()
            );
        }
    }

    public void centerTo(Point center) {
        int mapSizeX = mapWrapper.getMap().getArray().length * gameConfig.getPixelBlockSize();
        if (mapSizeX > gameConfig.getWindowWidth()) {
            offset.x = Math.max(
                    center.x - gameConfig.getWindowWidth() / 2,
                    0
            );
            if (offset.x + gameConfig.getWindowWidth() > mapSizeX) {
                offset.x = mapSizeX - gameConfig.getWindowWidth();
            }
        } else {
            offset.x = -(gameConfig.getWindowWidth() - mapSizeX) / 2;
        }

        int mapSizeY = mapWrapper.getMap().getArray()[0].length * gameConfig.getPixelBlockSize();
        if (mapSizeY > gameConfig.getWindowHeight()) {
            offset.y = Math.max(
                    center.y - gameConfig.getWindowHeight() / 2,
                    0
            );
            if (offset.y + gameConfig.getWindowHeight() > mapSizeY) {
                offset.y = mapSizeY - gameConfig.getWindowHeight();
            }
        } else {
            offset.y = -(gameConfig.getWindowHeight() - mapSizeY) / 2;
        }
    }
}
