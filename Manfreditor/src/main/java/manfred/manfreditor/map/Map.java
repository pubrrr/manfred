package manfred.manfreditor.map;

public class Map {
    private final String name;
    private final boolean[][] mapTiles;

    public Map(String name, boolean[][] mapTiles) {
        this.name = name;
        this.mapTiles = mapTiles;
    }

    public String getName() {
        return name;
    }

    public boolean[][] getArray() {
        return mapTiles;
    }

    public boolean isAccessible(int x, int y) {
        return isInBounds(x, y) && mapTiles[x][y];
    }

    private boolean isInBounds(int x, int y) {
        return true;
    }
}
