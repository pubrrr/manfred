package manfred.game.graphics;

import manfred.game.Manfred;
import manfred.game.controls.KeyControls;
import manfred.game.map.MapWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GamePanel extends JPanel {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 1200;
    public static final int PIXEL_BLOCK_SIZE = 40;

    private List<Paintable> paintables = new LinkedList<>();

    public GamePanel(MapWrapper mapWrapper, Manfred manfred, KeyControls keyControls) {
        super();
        setFocusable(true);
        requestFocus();
        addKeyListener(keyControls);

        registerPaintable(mapWrapper.getMap());
        registerPaintable(manfred);
    }

    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void registerPaintable(Paintable paintable) {
        paintables.add(paintable);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // TODO add some way to determine the order in which the elements get painted
        for (Paintable paintable : paintables) {
            paintable.paint(g);
        }
    }
}