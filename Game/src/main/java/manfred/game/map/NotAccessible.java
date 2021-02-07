package manfred.game.map;

import manfred.game.graphics.PanelCoordinate;

import java.awt.*;

public class NotAccessible implements MapTile {

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, PanelCoordinate coordinate) {
    }
}
