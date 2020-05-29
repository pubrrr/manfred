package manfred.game.map;

public class NotAccessible implements MapTile {
    @Override
    public boolean isAccessible() {
        return false;
    }
}
