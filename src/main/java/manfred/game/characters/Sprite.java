package manfred.game.characters;

import java.awt.*;

public class Sprite extends Rectangle {
    public Sprite(int x, int y, int sizeX, int sizeY) {
        super(x, y, sizeX, sizeY);
    }

    public Point getCenter() {
        return new Point(this.x + this.width / 2, this.y + this.height / 2);
    }

    public Polygon toPaint(Point offset) {
        return new Polygon(
                new int[]{x - offset.x, x - offset.x, x + width - offset.x, x + width - offset.x},
                new int[]{y - offset.y, y + height - offset.y, y + height - offset.y, y - offset.y},
                4
        );
    }

    public int getLeft() {
        return this.x;
    }

    public int getRight() {
        return this.x + this.width;
    }

    public int getTop() {
        return this.y;
    }

    public int getBottom() {
        return this.y + this.height;
    }
}
