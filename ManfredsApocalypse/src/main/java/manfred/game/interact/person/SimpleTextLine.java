package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.util.function.Function;

public class SimpleTextLine extends BasicTextLine implements TextLine {
    private final GelaberEdge edge;

    public SimpleTextLine(String[] textLines, GameConfig gameConfig, GelaberEdge edge) {
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
        return new GelaberResponseWrapper(
            successorSupplier.apply(edge.follow()),
            edge.getContinueCommand()
        );
    }
}
