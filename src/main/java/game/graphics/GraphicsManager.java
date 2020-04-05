package game.graphics;

import game.controls.KeyControls;
import game.map.Map;

public class GraphicsManager {
    private ManfredWindow window;

    public void initGraphics(Map map, KeyControls keyControls, ManfredWindow window) {
        window.add(new GamePanel(map, keyControls));
        window.pack();
        this.window = window;
    }

    public void paint() {
        window.repaint();
    }
}