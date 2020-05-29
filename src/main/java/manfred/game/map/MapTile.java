package manfred.game.map;

public interface MapTile {
    boolean isAccessible();

    void onStep();
}
