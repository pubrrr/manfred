package manfred.game.attack;

import manfred.game.graphics.Paintable;

import java.awt.*;

public class Attack implements Paintable {
    private int x;
    private int y;
    private int sizeX;
    private int sizeY;

    public Attack(int x, int y, int sizeX, int sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(x, y, sizeX, sizeY);
    }
}
