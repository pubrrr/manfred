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
import java.util.Stack;
import java.util.TreeMap;

public class GamePanel extends JPanel {
    public static final int FADE_PERIOD = 40;
    public static final int FADE_TRANSPARENCY_INTERVAL = 20;

    private final BackgroundScroller backgroundScroller;
    private GameConfig gameConfig;
    private PaintablesSorter paintablesSorter;

    private int fadeTransparency = 0;
    private List<PaintablesContainer> paintablesContainers = new LinkedList<>();

    public GamePanel(
        MapWrapper mapWrapper,
        Manfred manfred,
        EnemiesWrapper enemiesWrapper,
        AttacksContainer attacksContainer,
        BackgroundScroller backgroundScroller,
        GameConfig gameConfig,
        PaintablesSorter paintablesSorter
    ) {
        super();
        this.gameConfig = gameConfig;
        this.paintablesSorter = paintablesSorter;
        setFocusable(true);
        requestFocus();

        this.backgroundScroller = backgroundScroller;

        registerPaintableContainer(mapWrapper);
        registerPaintableContainer(() -> {
            Stack<PaintableContainerElement> elements = new Stack<>();
            elements.push(new PaintableContainerElement(manfred, manfred.getX(), manfred.getY()));
            return elements;
        });
        registerPaintableContainer(enemiesWrapper);
        registerPaintableContainer(attacksContainer);
    }

    public Dimension getPreferredSize() {
        return new Dimension(gameConfig.getWindowWidth(), gameConfig.getWindowHeight());
    }

    public void registerPaintableContainer(PaintablesContainer paintable) {
        paintablesContainers.add(paintable);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        TreeMap<Integer, TreeMap<Integer, Paintable>> paintablesSortedByYAndX = paintablesSorter.sortByYAndX(this.paintablesContainers);

        Point offset = backgroundScroller.getOffset();
        paintablesSortedByYAndX.forEach(
            (y, paintablesAtY) -> paintablesAtY.forEach(
                (x, paintable) -> {
                    paintable.paint(g, offset, x, y);
                }
            )
        );


        if (fadeTransparency > 0) {
            g.setColor(new Color(255, 255, 255, fadeTransparency));
            g.fillRect(0, 0, gameConfig.getWindowWidth(), gameConfig.getWindowHeight());
        }
    }

    public void deletePaintable(Paintable paintable) {
        // TODO rework
        paintablesContainers.remove(paintable);
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