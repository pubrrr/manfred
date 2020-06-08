package manfred.game.characters;

import java.awt.*;

public class Sprite extends Rectangle {
    public Sprite(int x, int y, int sizeX, int sizeY) {
        super(x, y, sizeX, sizeY);
    }

    public Point getCenter() {
        return new Point(this.x + this.width / 2, this.y + this.height / 2);
    }

    public Polygon toPaint() {
        return new Polygon(
                new int[]{x, x, x + width, x + width},
                new int[]{y, y + height, y + height, y},
                4
        );
    }

    public int left() {
        return this.x;
    }

    public int right() {
        return this.x + this.width;
    }

    public int top() {
        return this.y;
    }

    public int bottom() {
        return this.y + this.height;
    }
}
