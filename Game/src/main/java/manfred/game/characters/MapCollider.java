package manfred.game.characters;

import manfred.data.InvalidInputException;
import manfred.game.config.GameConfig;
import manfred.game.map.Map;
import manfred.game.map.MapFacade;
import org.springframework.stereotype.Component;

@Component
public class MapCollider {
    private static MapCollider instance = null;

    private final MapFacade mapFacade;
    private final GameConfig gameConfig;

    public MapCollider(MapFacade mapFacade, GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        instance = this;

        this.mapFacade = mapFacade;
    }

    public boolean collides(int leftBorder, int rightBorder, int topBorder, int bottomBorder) {
        int leftMapTile = leftBorder / gameConfig.getPixelBlockSize();
        int rightMapTile = rightBorder / gameConfig.getPixelBlockSize();
        int topMapTile = topBorder / gameConfig.getPixelBlockSize();
        int bottomMapTile = bottomBorder / gameConfig.getPixelBlockSize();

        for (int x = leftMapTile; x <= rightMapTile; x++) {
            for (int y = topMapTile; y <= bottomMapTile; y++) {
                if (!mapFacade.isAccessible(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static MapCollider getInstance() throws InvalidInputException {
        if (instance == null) {
            throw new InvalidInputException("Must call constuctor first.");
        }
        return instance;
    }
}
