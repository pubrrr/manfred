package manfred.game.map;

import java.awt.*;

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
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        // do nothing
    }
}
