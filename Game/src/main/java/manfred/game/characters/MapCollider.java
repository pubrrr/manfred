package manfred.game.characters;

import manfred.game.config.GameConfig;
import manfred.game.map.Map;
import manfred.game.map.MapWrapper;
import org.springframework.stereotype.Component;

@Component
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
        int leftMapTile = leftBorder / gameConfig.getPixelBlockSize();
        int rightMapTile = rightBorder / gameConfig.getPixelBlockSize();
        int topMapTile = topBorder / gameConfig.getPixelBlockSize();
        int bottomMapTile = bottomBorder / gameConfig.getPixelBlockSize();

        Map map = mapWrapper.getMap();
        for (int x = leftMapTile; x <= rightMapTile; x++) {
            for (int y = topMapTile; y <= bottomMapTile; y++) {
                if (!map.isAccessible(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static MapCollider getInstance() throws Exception {
        if (instance == null) {
            throw new Exception("Must call constuctor first.");
        }
        return instance;
    }
}
