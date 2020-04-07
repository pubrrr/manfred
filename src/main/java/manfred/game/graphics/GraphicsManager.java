package manfred.game.graphics;

import manfred.game.Manfred;
import manfred.game.controls.KeyControls;
import manfred.game.map.Map;

public class GraphicsManager {
    private ManfredWindow window;

    public void initGraphics(Map map, KeyControls keyControls, ManfredWindow window, Manfred manfred) {
        GamePanel panel = new GamePanel(map, manfred);

        window.add(panel);
        window.pack();

        panel.setFocusable(true);
        panel.requestFocus();
        panel.addKeyListener(keyControls);

        this.window = window;
    }

    public void paint() {
        window.repaint();
    }
}