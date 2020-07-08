package manfred.manfreditor.map;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class MapPane extends GridPane {
    public static final int PIXEL_BLOCK_SIZE = 50;

    private Rectangle[][] mapTiles;

    public void resetWithSize(int horizontalSize, int verticalSize) {
        resetChildren();
        setMinWidth(horizontalSize * PIXEL_BLOCK_SIZE);
        setMinHeight(verticalSize * PIXEL_BLOCK_SIZE);
        mapTiles = new Rectangle[horizontalSize][verticalSize];
    }

    private void resetChildren() {
        Node gridLines = getChildren().get(0);
        getChildren().clear();
        getChildren().add(0, gridLines);
    }

    public void addTile(Rectangle rectangle, int x, int y) {
        add(rectangle, x, y);
        mapTiles[x][y] = rectangle;
    }

    public Rectangle getTile(int x, int y) {
        return mapTiles[x][y];
    }
}
