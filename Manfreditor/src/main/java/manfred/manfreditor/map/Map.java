package manfred.manfreditor.map;

public class Map {
    private final String name;

    public Map(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isAccessible(int x, int y) {
        return true;
    }
}
