package manfred.game.map;

import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;

public class Accessible implements MapTile {
    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        // do nothing
    }
}
