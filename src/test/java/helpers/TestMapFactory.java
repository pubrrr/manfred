package helpers;

import manfred.game.GameConfig;
import manfred.game.interact.Interactable;
import manfred.game.map.*;
import org.springframework.lang.Nullable;

import java.util.HashMap;

import static org.mockito.Mockito.mock;

public class TestMapFactory {
    public static Map create(String[][] mapTilesAsStrings, @Nullable HashMap<String, Interactable> interactables) {
        MapTile[][] mapTiles = new MapTile[mapTilesAsStrings.length][mapTilesAsStrings[0].length];
        for (int x = 0; x < mapTilesAsStrings.length; x++) {
            for (int y = 0; y < mapTilesAsStrings[0].length; y++) {
                switch (mapTilesAsStrings[x][y]) {
                    case MapReader.ACCESSIBLE:
                        mapTiles[x][y] = new Accessible();
                        break;
                    case MapReader.NOT_ACCESSIBLE:
                        mapTiles[x][y] = new NotAccessible(null, null, 1);
                        break;
                    default:
                        mapTiles[x][y] = interactables.get(mapTilesAsStrings[x][y]);
                }
            }
        }
        return new Map("testName", mapTiles, mock(GameConfig.class));
    }
}
