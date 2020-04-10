package manfred.game.map;

import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;

import java.awt.*;

public class Map implements Paintable {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";

    private String name;
    private String[][] mapArray;

    public Map(String name, String[][] map) {
        this.name = name;
        this.mapArray = map;
    }

    public String getName() {
        return name;
    }

    public String[][] getArray() {
        return mapArray;
    }

    public boolean isAccessible(int x, int y) {
        return x >= 0 && x < mapArray.length
            && y >= 0 && y < mapArray[0].length
            && mapArray[x][y].equals(Map.ACCESSIBLE);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);

        for (int x = 0; x < getArray().length; x++) {
            for (int y = 0; y < getArray()[0].length; y++) {
                if (!isAccessible(x, y)) {
                    g.fillRect(GamePanel.PIXEL_BLOCK_SIZE * x, GamePanel.PIXEL_BLOCK_SIZE * y, GamePanel.PIXEL_BLOCK_SIZE, GamePanel.PIXEL_BLOCK_SIZE);
                }
            }
        }
    }
}
