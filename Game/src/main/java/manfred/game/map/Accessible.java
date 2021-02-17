package manfred.game.map;

import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;

public class Accessible implements MapTile {
    private static final Accessible instance = new Accessible();

    private Accessible() {
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    public static Accessible getInstance() {
        return instance;
    }

    @Override
    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        // do nothing
    }
}
