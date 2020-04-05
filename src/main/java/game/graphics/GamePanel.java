package game.graphics;

import game.controls.KeyControls;
import game.map.Map;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 1200;
    private static final int PIXEL_BLOCK_SIZE = 40;

    private Map map;

    public GamePanel(Map map, KeyControls keyControls) {
        super();
        this.map = map;
        addKeyListener(keyControls);
    }

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintMap(g);
    }

    private void paintMap(Graphics g) {
        g.setColor(Color.RED);

        for (int x = 0; x < map.getMap().length; x++) {
            for (int y = 0; y < map.getMap()[0].length; y++) {
                if (!map.isAccessible(x, y)) {
                    g.fillRect(PIXEL_BLOCK_SIZE * x, PIXEL_BLOCK_SIZE * y, PIXEL_BLOCK_SIZE, PIXEL_BLOCK_SIZE);
                }
            }
        }
    }
}