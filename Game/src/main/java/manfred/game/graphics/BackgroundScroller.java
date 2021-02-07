package manfred.game.graphics;

import manfred.game.config.GameConfig;
import manfred.game.geometry.Vector;
import manfred.game.map.MapFacade;

public class BackgroundScroller {
    private final int triggerScrollDistanceToBorder;
    private final MapFacade mapFacade;
    private final GameConfig gameConfig;

    private Vector<PanelCoordinate> offset = Vector.zero();

    public BackgroundScroller(int triggerScrollDistanceToBorder, MapFacade mapFacade, GameConfig gameConfig) {
        this.triggerScrollDistanceToBorder = triggerScrollDistanceToBorder;
        this.mapFacade = mapFacade;
        this.gameConfig = gameConfig;
    }

    public Vector<PanelCoordinate> getOffset(PanelCoordinate manfredCenterPanelCoordinate) {
        this.offset = Vector.of(
            updateX(manfredCenterPanelCoordinate),
            updateY(manfredCenterPanelCoordinate)
        );
        return this.offset;
    }

    private int updateX(PanelCoordinate manfredCenterPanelCoordinate) {
        int mapSizeX = mapFacade.getMapSizeX() * gameConfig.getPixelBlockSize().value();
        if (mapSizeX < gameConfig.getWindowWidth()) {
            return (gameConfig.getWindowWidth() - mapSizeX) / 2;
        }

        int distanceToLeftBorder = manfredCenterPanelCoordinate.getX() + offset.x();
        int distanceToRightBorder = gameConfig.getWindowWidth() - (manfredCenterPanelCoordinate.getX() + offset.x());

        if (distanceToLeftBorder < triggerScrollDistanceToBorder) {
            return -Math.max(
                -offset.x() - (triggerScrollDistanceToBorder - distanceToLeftBorder),
                0
            );
        } else if (distanceToRightBorder < triggerScrollDistanceToBorder) {
            return -Math.min(
                -offset.x() + (triggerScrollDistanceToBorder - distanceToRightBorder),
                mapSizeX - gameConfig.getWindowWidth()
            );
        }
        return offset.x();
    }

    private int updateY(PanelCoordinate manfredCenterPanelCoordinate) {
        int mapSizeY = mapFacade.getMapSizeY() * gameConfig.getPixelBlockSize().value();
        if (mapSizeY < gameConfig.getWindowHeight()) {
            return (gameConfig.getWindowHeight() - mapSizeY) / 2;
        }

        int distanceToTopBorder = manfredCenterPanelCoordinate.getY() + offset.y();
        int distanceToBottomBorder = gameConfig.getWindowHeight() - (manfredCenterPanelCoordinate.getY() + offset.y());

        if (distanceToTopBorder < triggerScrollDistanceToBorder) {
            return -Math.max(
                -offset.y() - (triggerScrollDistanceToBorder - distanceToTopBorder),
                0
            );
        } else if (distanceToBottomBorder < triggerScrollDistanceToBorder) {
            return -Math.min(
                -offset.y() + (triggerScrollDistanceToBorder - distanceToBottomBorder),
                mapSizeY - gameConfig.getWindowHeight()
            );
        }
        return offset.y();
    }

    public void centerTo(PanelCoordinate center) {
        int mapSizeX = mapFacade.getMapSizeX() * gameConfig.getPixelBlockSize().value();
        int offsetX;
        int offsetY;

        if (mapSizeX > gameConfig.getWindowWidth()) {
            offsetX = -Math.max(
                    center.getX() - gameConfig.getWindowWidth() / 2,
                    0
            );
            if (-offsetX + gameConfig.getWindowWidth() > mapSizeX) {
                offsetX = mapSizeX - gameConfig.getWindowWidth();
            }
        } else {
            offsetX = (gameConfig.getWindowWidth() - mapSizeX) / 2;
        }

        int mapSizeY = mapFacade.getMapSizeY() * gameConfig.getPixelBlockSize().value();
        if (mapSizeY > gameConfig.getWindowHeight()) {
            offsetY = - Math.max(
                    center.getY() - gameConfig.getWindowHeight() / 2,
                    0
            );
            if (-offsetY + gameConfig.getWindowHeight() > mapSizeY) {
                offsetY = mapSizeY - gameConfig.getWindowHeight();
            }
        } else {
            offsetY = (gameConfig.getWindowHeight() - mapSizeY) / 2;
        }

        this.offset = Vector.of(offsetX, offsetY);
    }
}
