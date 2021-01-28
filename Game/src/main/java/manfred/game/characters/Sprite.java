package manfred.game.characters;

import java.awt.*;

public class Sprite extends Rectangle {
    private final int baseHeight;

    public Sprite(int x, int y, int width, int spriteHeight, int baseHeight) {
        super(x, y, width, spriteHeight);
        this.baseHeight = baseHeight;
    }

    public Point getCenter() {
        return new Point(this.x + this.width / 2, this.y + this.height / 2);
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

    public int getBaseHeight() {
        return this.baseHeight;
    }

    public int getBaseTop() {
        return this.y + this.height - this.baseHeight;
    }

    public int getSpriteHeight() {
        return this.height;
    }
}
