package manfred.game.graphics;

import manfred.game.characters.sprite.LocatedSprite;

import java.awt.*;

public class GraphicsAdapter {
    private final Graphics graphics;

    public GraphicsAdapter(Graphics graphics) {
        this.graphics = graphics;
    }

    public void drawImage(Image img, int x, int y, int width, int height) {
        graphics.drawImage(img, x, y, width, height, null);
    }

    public void setColor(Color color) {
        graphics.setColor(color);
    }

    public void drawRectangle(int x, int y, int width, int height) {
        graphics.drawRect(x, y, width, height);
    }

    public void fillRectangle(int x, int y, int width, int height) {
        graphics.fillRect(x, y, width, height);
    }

    public void setFont(Font font) {
        graphics.setFont(font);
    }

    public void drawString(String string, int x, int y) {
        graphics.drawString(string, x, y);
    }

    public void drawSprite(LocatedSprite sprite) {
        PanelCoordinate topLeft = sprite.getTopLeft();

        graphics.drawImage(
            sprite.getImage(),
            topLeft.getX(),
            topLeft.getY(),
            sprite.getWidth().value(),
            sprite.getHeight().value(),
            null
        );
    }
}
