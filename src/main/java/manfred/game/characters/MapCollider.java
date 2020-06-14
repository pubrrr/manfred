package manfred.game.characters;

import manfred.game.GameConfig;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;

public class MapCollider {
    private static MapCollider instance = null;

    private MapWrapper mapWrapper;
    private GameConfig gameConfig;

    public MapCollider(MapWrapper mapWrapper, GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        instance = this;

        this.mapWrapper = mapWrapper;
    }

    public boolean collides(int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        int onMapGripLeft = leftBorder / gameConfig.getPixelBlockSize();
        int onMapGripRight = rightBorder / gameConfig.getPixelBlockSize();
        int onMapGripTop = topBorder / gameConfig.getPixelBlockSize();
        int onMapGripBottom = bottomBorder / gameConfig.getPixelBlockSize();

        Map map = mapWrapper.getMap();
        return !(
                map.isAccessible(onMapGripLeft, onMapGripTop)
                        && map.isAccessible(onMapGripRight, onMapGripTop)
                        && map.isAccessible(onMapGripLeft, onMapGripBottom)
                        && map.isAccessible(onMapGripRight, onMapGripBottom)
        );
    }

    public static MapCollider getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Must call constuctor first.");
        }
        return instance;
    }
}
