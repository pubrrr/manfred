package manfred.game.graphics;

import manfred.game.GameConfig;
import manfred.game.attack.AttacksContainer;
import manfred.game.characters.Manfred;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.map.MapWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GamePanel extends JPanel {
    public static final int FADE_PERIOD = 40;
    public static final int FADE_TRANSPARENCY_INTERVAL = 20;

    private final BackgroundScroller backgroundScroller;
    private GameConfig gameConfig;

    private int fadeTransparency = 0;
    private List<Paintable> paintables = new LinkedList<>();

    public GamePanel(
            MapWrapper mapWrapper,
            Manfred manfred,
            EnemiesWrapper enemiesWrapper,
            AttacksContainer attacksContainer,
            BackgroundScroller backgroundScroller,
            GameConfig gameConfig
    ) {
        super();
        this.gameConfig = gameConfig;
        setFocusable(true);
        requestFocus();

        this.backgroundScroller = backgroundScroller;

        registerPaintable(mapWrapper);
        registerPaintable(manfred);
        registerPaintable(enemiesWrapper);
        registerPaintable(attacksContainer);
    }

    public Dimension getPreferredSize() {
        return new Dimension(gameConfig.getWindowWidth(), gameConfig.getWindowHeight());
    }

    public void registerPaintable(Paintable paintable) {
        paintables.add(paintable);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // TODO add some way to determine the order in which the elements get painted
        Point offset = backgroundScroller.getOffset();
        for (Paintable paintable : paintables) {
            paintable.paint(g, offset);
        }

        if (fadeTransparency > 0) {
            g.setColor(new Color(255, 255, 255, fadeTransparency));
            g.fillRect(0, 0, gameConfig.getWindowWidth(), gameConfig.getWindowHeight());
        }
    }

    public void deletePaintable(Paintable paintable) {
        paintables.remove(paintable);
    }

    public void fadeOut() {
        while (fadeTransparency + FADE_TRANSPARENCY_INTERVAL < 255) {
            fadeTransparency += FADE_TRANSPARENCY_INTERVAL;
            sleep();
        }
        fadeTransparency = 255;
        sleep();
    }

    public void fadeIn() {
        while (fadeTransparency > FADE_TRANSPARENCY_INTERVAL) {
            fadeTransparency -= FADE_TRANSPARENCY_INTERVAL;
            sleep();
        }
        fadeTransparency = 0;
    }

    private void sleep() {
        try {
            Thread.sleep(FADE_PERIOD);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}