package helpers;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import manfred.game.map.Accessible;
import manfred.game.map.Map;
import manfred.game.map.MapTile;
import manfred.game.map.NotAccessible;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestMapFactory {

    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";

    public static Map create(String[][] mapTilesAsStrings) {
        return create(mapTilesAsStrings, new HashMap<>());
    }

    public static Map create(String[][] mapTilesAsStrings, HashMap<String, MapTile> interactables) {
        String[][] transposed = transpose(mapTilesAsStrings);

        int sizeX = transposed.length;
        int sizeY = transposed[0].length;
        java.util.Map<MapPrototype.Coordinate, MapTile> mapTiles = new HashMap<>();
        for (int x = 0; x < sizeX; x++) {
            String[] rowsAsStrings = transposed[x];
            for (int y = 0; y < sizeY; y++) {
                String tileValue = rowsAsStrings[y];
                MapTile tile;
                switch (tileValue) {
                    case ACCESSIBLE:
                        tile = new Accessible();
                        break;
                    case NOT_ACCESSIBLE:
                        tile = new NotAccessible();
                        break;
                    default:
                        tile = interactables.get(tileValue);
                        break;
                }
                mapTiles.put(new CoordinateDouble(PositiveInt.of(x), PositiveInt.of(y)), tile);
            }
        }
        return new Map(mapTiles);
    }

    private static String[][] transpose(String[][] input) {
        String[][] transposed = new String[input[0].length][input.length];

        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                transposed[j][i] = input[i][j] != null ? input[i][j] : "1";
            }
        }

        return transposed;
    }

    public static Map defaultCoordinateProvider() {
        return create(new String[][]{
            {"1", "1", "1", "1", "1"},
            {"1", "1", "1", "1", "1"},
            {"1", "1", "1", "1", "1"},
            {"1", "1", "1", "1", "1"},
            {"1", "1", "1", "1", "1"},
        });
    }

    public static Map.Coordinate coordinateAt(int x, int y) {
        return defaultCoordinateProvider().coordinateAt(x, y);
    }

    public static Map.TileCoordinate tileAt(PositiveInt x, PositiveInt y) {
        return defaultCoordinateProvider().tileAt(x, y);
    }

    public static class CoordinateDouble extends MapPrototype.Coordinate {

        public CoordinateDouble(int x, int y) {
            this(PositiveInt.of(x), PositiveInt.of(y));
        }

        public CoordinateDouble(PositiveInt x, PositiveInt y) {
            super(x, y);
        }
    }
}
