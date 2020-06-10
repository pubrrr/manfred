package manfred.game.map;

import com.sun.istack.internal.Nullable;
import manfred.game.controls.KeyControls;
import manfred.game.graphics.GamePanel;
import manfred.game.interact.Interactable;

import java.awt.*;
import java.util.function.Consumer;

public class Map {
    private String name;
    private MapTile[][] mapTiles;

    public Map(String name, MapTile[][] mapTiles) {
        this.name = name;
        this.mapTiles = mapTiles;
    }

    // TODO necessary?
    public String getName() {
        return name;
    }

    public MapTile[][] getArray() {
        return mapTiles;
    }

    public boolean isAccessible(int x, int y) {
        return isInBounds(x, y) && mapTiles[x][y].isAccessible();
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < mapTiles.length
                && y >= 0 && y < mapTiles[0].length;
    }

    public void paint(Graphics g, Point offset) {
        g.setColor(Color.RED);

        for (int x = 0; x < mapTiles.length; x++) {
            for (int y = 0; y < mapTiles[0].length; y++) {
                if (mapTiles[x][y] instanceof Interactable) {
                    g.setColor(Color.YELLOW);
                }
                if (!isAccessible(x, y)) {
                    g.fillRect(
                            GamePanel.PIXEL_BLOCK_SIZE * x - offset.x,
                            GamePanel.PIXEL_BLOCK_SIZE * y - offset.y,
                            GamePanel.PIXEL_BLOCK_SIZE,
                            GamePanel.PIXEL_BLOCK_SIZE
                    );
                }
                if (mapTiles[x][y] instanceof Interactable) {
                    g.setColor(Color.RED);
                }
            }
        }
    }

    @Nullable
    public Interactable getInteractable(int x, int y) {
        return mapTiles[x][y] instanceof Interactable
                ? (Interactable) mapTiles[x][y]
                : null;
    }

    @Nullable
    public Consumer<KeyControls> stepOn(int x, int y) {
        return mapTiles[x][y].onStep();
    }
}
