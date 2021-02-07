package manfred.game.graphics;

import manfred.game.attack.AttacksContainer;
import manfred.game.attack.Caster;
import manfred.game.characters.Manfred;
import manfred.game.config.GameConfig;
import manfred.game.enemy.EnemiesWrapper;
import manfred.game.graphics.coordinatetransformation.MapCoordinateToPanelCoordinateTransformer;
import manfred.game.graphics.paintable.GelaberOverlay;
import manfred.game.graphics.paintable.LocatedPaintable;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import manfred.game.map.MapFacade;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.Stack;

@Component
public class GamePanel extends JPanel {
    public static final int FADE_PERIOD = 40;
    public static final int FADE_TRANSPARENCY_INTERVAL = 20;

    private final BackgroundScroller backgroundScroller;
    private final GameConfig gameConfig;
    private final PaintablesSorter paintablesSorter;
    private final GelaberOverlay gelaberOverlay;
    private final MapCoordinateToPanelCoordinateTransformer mapCoordinateToPanelCoordinateTransformer;

    private int fadeTransparency = 0;
    private final List<PaintablesContainer> paintablesContainers = new LinkedList<>();

    public GamePanel(
        MapFacade mapFacade,
        Manfred manfred,
        Caster attackCaster,
        EnemiesWrapper enemiesWrapper,
        AttacksContainer attacksContainer,
        BackgroundScroller backgroundScroller,
        GameConfig gameConfig,
        PaintablesSorter paintablesSorter,
        GelaberOverlay gelaberOverlay,
        MapCoordinateToPanelCoordinateTransformer mapCoordinateToPanelCoordinateTransformer
    ) {
        super();
        this.gameConfig = gameConfig;
        this.paintablesSorter = paintablesSorter;
        this.gelaberOverlay = gelaberOverlay;
        this.mapCoordinateToPanelCoordinateTransformer = mapCoordinateToPanelCoordinateTransformer;
        setFocusable(true);
        requestFocus();

        this.backgroundScroller = backgroundScroller;

        registerPaintableContainer(mapFacade);
        registerPaintableContainer(() -> {
            Stack<PaintableContainerElement> elements = new Stack<>();
            elements.push(new PaintableContainerElement(attackCaster, manfred.getTopLeft()));
            elements.push(new PaintableContainerElement(manfred, manfred.getTopLeft()));
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

        SortedMap<PanelCoordinate, LocatedPaintable> paintablesSortedByYAndX = paintablesSorter.sortByYAndX(
            this.paintablesContainers,
            mapCoordinateToPanelCoordinateTransformer
        );

        paintablesSortedByYAndX.forEach(
            (coordinate, paintable) -> paintable.paint(g, offset, coordinate.getX(), coordinate.getY())
        );

        gelaberOverlay.paint(g);

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
}