package manfred.game.graphics.scrolling;

import manfred.game.config.GameConfig;
import manfred.game.geometry.Vector;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.map.MapFacade;

public class BackgroundScroller {
    private final int triggerScrollDistanceToBorder;
    private final MapFacade mapFacade;
    private final GameConfig gameConfig;

    private CoordinateScroller xScroller = new ConstantScroller(0);
    private CoordinateScroller yScroller = new ConstantScroller(0);

    private BackgroundScroller(int triggerScrollDistanceToBorder, MapFacade mapFacade, GameConfig gameConfig) {
        this.triggerScrollDistanceToBorder = triggerScrollDistanceToBorder;
        this.mapFacade = mapFacade;
        this.gameConfig = gameConfig;
    }

    public static Factory factoryWith(int triggerScrollDistanceToBorder, MapFacade mapFacade, GameConfig gameConfig) {
        return new Factory(triggerScrollDistanceToBorder, mapFacade, gameConfig);
    }

    public Vector<PanelCoordinate> getOffset(PanelCoordinate manfredCenterPanelCoordinate) {
        return Vector.of(
            xScroller.computeScrollDistance(manfredCenterPanelCoordinate.getX()),
            yScroller.computeScrollDistance(manfredCenterPanelCoordinate.getY())
        );
    }

    public void centerTo(PanelCoordinate center) {
        int mapSizeX = mapFacade.getMapSizeX() * gameConfig.getPixelBlockSize().value();
        xScroller = CoordinateScroller.buildFrom(triggerScrollDistanceToBorder, gameConfig.getWindowWidth(), mapSizeX, center.getX());

        int mapSizeY = mapFacade.getMapSizeY() * gameConfig.getPixelBlockSize().value();
        yScroller = CoordinateScroller.buildFrom(triggerScrollDistanceToBorder, gameConfig.getWindowHeight(), mapSizeY, center.getY()).inverted();
    }

    public static class Factory {
        private final int triggerScrollDistanceToBorder;
        private final MapFacade mapFacade;
        private final GameConfig gameConfig;

        public Factory(int triggerScrollDistanceToBorder, MapFacade mapFacade, GameConfig gameConfig) {
            this.triggerScrollDistanceToBorder = triggerScrollDistanceToBorder;
            this.mapFacade = mapFacade;
            this.gameConfig = gameConfig;
        }

        public BackgroundScroller buildCenteredAt(PanelCoordinate center) {
            BackgroundScroller backgroundScroller = new BackgroundScroller(triggerScrollDistanceToBorder, mapFacade, gameConfig);
            backgroundScroller.centerTo(center);
            return backgroundScroller;
        }
    }
}
