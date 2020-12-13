package manfred.game.graphics;

import manfred.game.GameConfig;
import manfred.game.attack.AttacksContainer;
import manfred.game.attack.Caster;
import manfred.game.characters.Manfred;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.interact.gelaber.Gelaber;
import manfred.game.map.MapWrapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

@Component
public class GamePanel extends JPanel {
    public static final int FADE_PERIOD = 40;
    public static final int FADE_TRANSPARENCY_INTERVAL = 20;

    private final BackgroundScroller backgroundScroller;
    private final GameConfig gameConfig;
    private final PaintablesSorter paintablesSorter;

    private int fadeTransparency = 0;
    private final List<PaintablesContainer> paintablesContainers = new LinkedList<>();
    @Nullable private Gelaber gelaber = null;

    public GamePanel(
        MapWrapper mapWrapper,
        Manfred manfred,
        Caster attackCaster,
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
            elements.push(new PaintableContainerElement(attackCaster, manfred.getX() - gameConfig.getPixelBlockSize() / 2, manfred.getY()));
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

        Point offset = backgroundScroller.getOffset();

        TreeMap<Integer, TreeMap<Integer, Paintable>> paintablesSortedByYAndX = paintablesSorter.sortByYAndX(this.paintablesContainers);
        paintablesSortedByYAndX.forEach(
            (y, paintablesAtY) -> paintablesAtY.forEach(
                (x, paintable) -> {
                    paintable.paint(g, offset, x, y);
                }
            )
        );

        if (this.gelaber != null) {
            gelaber.paint(g, offset, 0, 0);
        }

        if (fadeTransparency > 0) {
            g.setColor(new Color(255, 255, 255, fadeTransparency));
            g.fillRect(0, 0, gameConfig.getWindowWidth(), gameConfig.getWindowHeight());
        }
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

    public void registerGelaberToPaint(Gelaber gelaber) {
        this.gelaber = gelaber;
    }

    public void deleteGelaber() {
        this.gelaber = null;
    }
}