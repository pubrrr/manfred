package manfred.game.map;

import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;

public class NotAccessible implements MapTile {

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
    }
}
