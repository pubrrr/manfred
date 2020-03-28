package game.graphics;

import game.map.Map;

import javax.swing.*;

public class GraphicsManager {
    private JFrame window;

    public void init(Map map) {
        this.window = new ManfredFrame();
        window.add(new GamePanel(map));
        window.pack();
    }

    public void paint() {
        window.repaint();
    }
}