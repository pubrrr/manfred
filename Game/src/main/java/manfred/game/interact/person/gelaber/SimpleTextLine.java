package manfred.game.interact.person.gelaber;

import manfred.game.config.GameConfig;

import java.util.List;
import java.util.function.Function;

public class SimpleTextLine extends BasicTextLine implements TextLine {
    private final GelaberEdge edge;

    public SimpleTextLine(List<String> textLines, GameConfig gameConfig, GelaberEdge edge) {
        super(textLines, gameConfig);
        this.edge = edge;
    }

    @Override
    public void up() {
    }

    @Override
    public void down() {
    }

    @Override
    public GelaberResponseWrapper next(Function<GelaberNodeIdentifier, TextLine> successorSupplier) {
        return GelaberResponseWrapper.followingTheEdge(edge, successorSupplier);
    }
}
