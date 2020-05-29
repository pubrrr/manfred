package manfred.game.map;

public class Accessible implements MapTile{
    private static Accessible instance;

    @Override
    public boolean isAccessible() {
        return true;
    }

    public static Accessible getInstance() {
        if (instance == null) {
            instance = new Accessible();
        }
        return instance;
    }
}
