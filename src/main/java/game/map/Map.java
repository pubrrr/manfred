package game.map;

public class Map {
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
}
