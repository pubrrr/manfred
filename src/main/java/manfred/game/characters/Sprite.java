package manfred.game.characters;

import manfred.game.graphics.Paintable;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sprite extends Rectangle implements Paintable {
    private BufferedImage image;

    public Sprite(int x, int y, int sizeX, int sizeY, BufferedImage image) {
        super(x, y, sizeX, sizeY);
        this.image = image;
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

    @Override
    public void paint(Graphics g, Point offset) {
        g.drawImage(image, x - offset.x, y - offset.y, width, height, null);
    }
}
