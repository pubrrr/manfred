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

    public static Map create(String[][] mapTilesAsStrings) {
        return create(mapTilesAsStrings, new HashMap<>());
    }

    public static Map create(String[][] mapTilesAsStrings, HashMap<String, Interactable> interactables) {
        String[][] transposed = transpose(mapTilesAsStrings);

        List<List<MapTile>> mapTiles = new ArrayList<>(transposed.length);
        for (String[] rowsAsStrings : transposed) {
            List<MapTile> column = new ArrayList<>(transposed[0].length);
            for (String tileValue : rowsAsStrings) {
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

    private static String[][] transpose(String[][] input) {
        String[][] transposed = new String[input[0].length][input.length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                transposed[j][i] = input[i][j];
            }
        }

        return transposed;
    }
}
