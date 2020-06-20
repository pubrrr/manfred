package manfred.game.graphics;

public class PaintableContainerElement {
    private Paintable paintable;
    private int x;
    private int y;

    public PaintableContainerElement(Paintable paintable, int x, int y) {
        this.paintable = paintable;
        this.x = x;
        this.y = y;
    }

    public Paintable getPaintable() {
        return paintable;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
