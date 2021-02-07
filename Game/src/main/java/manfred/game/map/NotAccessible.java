package manfred.game.map;

import java.awt.*;

public class NotAccessible implements MapTile {

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Integer x, Integer y) {
    }
}
