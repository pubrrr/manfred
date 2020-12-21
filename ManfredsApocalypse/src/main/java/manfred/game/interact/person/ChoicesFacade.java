package manfred.game.interact.person;

import manfred.game.GameConfig;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ChoicesFacade implements Paintable {
    private final GameConfig gameConfig;
    private final LinkedHashMap<GelaberNodeIdentifier, GelaberEdge> edgesToChooseFrom;
    private final Selector selector;

    public ChoicesFacade(LinkedHashMap<GelaberNodeIdentifier, GelaberEdge> edgesToChooseFrom, Selector selector, GameConfig gameConfig) {
        this.edgesToChooseFrom = edgesToChooseFrom;
        this.selector = selector;
        this.gameConfig = gameConfig;
    }

    public void up() {
        selector.selectPrevious();
    }

    public void down() {
        selector.selectNext();
    }

    public GelaberEdge confirm() {
        return edgesToChooseFrom.get(this.selector.confirm());
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.setColor(Color.YELLOW);
        g.fillRect(gameConfig.getGelaberBoxPositionX(), gameConfig.getGelaberBoxPositionY(), 500, 500);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getTextPointSize()));

        AtomicInteger idx = new AtomicInteger();
        edgesToChooseFrom.forEach((nodeIdentifier, edge) -> {
            if (selector.isSelected(nodeIdentifier)) {
//                selectionMarker.paint(g); // TODO
            }
            g.drawString(
                edge.getEdgeText(),
                gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox(),
                gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + idx.getAndIncrement() * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()) + gameConfig.getTextPointSize() / 2
            );
        });
    }

    public static ChoicesFacadeBuilder buildWith(GameConfig gameConfig) {
        return new ChoicesFacadeBuilder(gameConfig);
    }
}
