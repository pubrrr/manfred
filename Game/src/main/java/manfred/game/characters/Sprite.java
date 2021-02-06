package manfred.game.characters;

public class Sprite {
    private final int width;
    private final int height;

    public Sprite(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getSpriteHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
