package helpers;

import manfred.game.config.GameConfig;
import manfred.game.interact.Interactable;
import manfred.game.map.Accessible;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.game.map.NotAccessible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestMapFactory {

    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";

    public static Map create(String[][] mapTilesAsStrings, HashMap<String, Interactable> interactables) {
        List<List<MapTile>> mapTiles = new ArrayList<>(mapTilesAsStrings.length);
        for (String[] columnAsStrings : mapTilesAsStrings) {
            List<MapTile> column = new ArrayList<>(mapTilesAsStrings[0].length);
            for (String tileValue : columnAsStrings) {
                MapTile tile;
                switch (tileValue) {
                    case ACCESSIBLE:
                        tile = Accessible.getInstance();
                        break;
                    case NOT_ACCESSIBLE:
                        tile = new NotAccessible();
                        break;
                    default:
                        tile = interactables.get(tileValue);
                        break;
                }
                column.add(tile);
            }
            mapTiles.add(column);
        }
        return new Map(mapTiles, mock(GameConfig.class));
    }
}
