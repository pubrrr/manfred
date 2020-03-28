package game.map;

public class Map {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";

    private String name;
    private String[][] map;

    public Map(String name, String[][] map) {
        this.name = name;
        this.map = map;
    }

    public String getName() {
        return name;
    }

    public String[][] getMap() {
        return map;
    }

    public boolean isAccessible(int x, int y) {
        return x >= 0 && x < map.length
            && y >= 0 && y < map[0].length
            && map[x][y].equals(Map.ACCESSIBLE);
    }
}
