package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;
import manfred.game.graphics.paintable.Paintable;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChoicesFacade implements Paintable {
    private final GameConfig gameConfig;
    private final java.util.List<GelaberEdge> edgesToChooseFrom;
    private final Selector selector;

    public ChoicesFacade(List<GelaberEdge> edgesToChooseFrom, Selector selector, GameConfig gameConfig) {
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
        return this.selector.confirm();
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.setColor(Color.YELLOW);
        g.fillRect(gameConfig.getGelaberBoxPositionX(), gameConfig.getGelaberBoxPositionY(), 500, 500);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Palatino Linotype", Font.BOLD, gameConfig.getTextPointSize()));

        this.selector.paint(g, offset, x, y);

        AtomicInteger idx = new AtomicInteger();
        edgesToChooseFrom.forEach(edge -> g.drawString(
            edge.getEdgeText(),
            gameConfig.getGelaberBoxPositionX() + gameConfig.getTextDistanceToBox(),
            gameConfig.getGelaberBoxPositionY() + gameConfig.getTextDistanceToBox() + idx.getAndIncrement() * (gameConfig.getTextPointSize() + gameConfig.getDistanceBetweenLines()) + gameConfig.getTextPointSize() / 2
        ));
    }

    public static ChoicesFacadeBuilder buildWith(GameConfig gameConfig) {
        return new ChoicesFacadeBuilder(gameConfig);
    }
}
