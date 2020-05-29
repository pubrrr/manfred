package manfred.game.map;

public class NotAccessible implements MapTile {
    private static NotAccessible instance;

    @Override
    public boolean isAccessible() {
        return false;
    }

    public static NotAccessible getInstance() {
        if (instance == null) {
            instance = new NotAccessible();
        }
        return instance;
    }
}
