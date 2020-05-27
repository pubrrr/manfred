package manfred.game.map;

import com.sun.istack.internal.Nullable;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;
import manfred.game.interact.Interact;

import java.awt.*;
import java.util.HashMap;
import java.util.function.Consumer;

public class Map implements Paintable {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";

    private String name;
    private String[][] mapArray;
    private HashMap<String, Interact> interacts;

    public Map(String name, String[][] map, HashMap<String, Interact> interacts) {
        this.name = name;
        this.mapArray = map;
        this.interacts = interacts;
    }

    public String getName() {
        return name;
    }

    public String[][] getArray() {
        return mapArray;
    }

    public boolean isAccessible(int x, int y) {
        return isInBounds(x, y)  && mapArray[x][y].equals(Map.ACCESSIBLE);
    }

    public boolean isSpecialField(int x, int y) {
        return isInBounds(x, y) && !mapArray[x][y].equals(Map.ACCESSIBLE) && !mapArray[x][y].equals(Map.NOT_ACCESSIBLE);
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < mapArray.length
                && y >= 0 && y < mapArray[0].length;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.RED);

        for (int x = 0; x < getArray().length; x++) {
            for (int y = 0; y < getArray()[0].length; y++) {
                if (mapArray[x][y].equals("Opa")) {
                    g.setColor(Color.YELLOW);
                }
                if (!isAccessible(x, y)) {
                    g.fillRect(GamePanel.PIXEL_BLOCK_SIZE * x, GamePanel.PIXEL_BLOCK_SIZE * y, GamePanel.PIXEL_BLOCK_SIZE, GamePanel.PIXEL_BLOCK_SIZE);
                }
                if (mapArray[x][y].equals("Opa")) {
                    g.setColor(Color.RED);
                }
            }
        }
    }

    @Nullable
    public Interact getInteract(int x, int y) {
        if (!isSpecialField(x, y)) {
            return null;
        }
        return interacts.get(mapArray[x][y]);
    }
}
