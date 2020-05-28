package manfred.game.map;

import com.sun.istack.internal.Nullable;
import manfred.game.graphics.GamePanel;
import manfred.game.graphics.Paintable;
import manfred.game.interact.Interactable;

import java.awt.*;
import java.util.HashMap;

public class Map implements Paintable {
    public static final String ACCESSIBLE = "1";
    public static final String NOT_ACCESSIBLE = "0";

    private String name;
    private String[][] mapArray;
    private HashMap<String, Interactable> interactables;

    public Map(String name, String[][] map, HashMap<String, Interactable> interactables) {
        this.name = name;
        this.mapArray = map;
        this.interactables = interactables;
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
    public Interactable getInteractable(int x, int y) {
        if (!isSpecialField(x, y)) {
            return null;
        }
        return interactables.get(mapArray[x][y]);
    }
}
