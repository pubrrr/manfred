package helpers;

import manfred.game.config.GameConfig;
import manfred.game.interact.Interactable;
import manfred.game.map.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

public class TestMapFactory {
    public static Map create(String[][] mapTilesAsStrings, HashMap<String, Interactable> interactables) {
        List<List<MapTile>> mapTiles = new ArrayList<>(mapTilesAsStrings.length);
        for (String[] columnAsStrings : mapTilesAsStrings) {
            List<MapTile> column = new ArrayList<>(mapTilesAsStrings[0].length);
            for (String tileValue : columnAsStrings) {
                MapTile tile;
                switch (tileValue) {
                    case MapConverter.ACCESSIBLE -> tile = Accessible.getInstance();
                    case MapConverter.NOT_ACCESSIBLE -> tile = new NotAccessible();
                    default -> tile = interactables.get(tileValue);
                }
                column.add(tile);
            }
            mapTiles.add(column);
        }
        return new Map(mapTiles, mock(GameConfig.class));
    }
}
