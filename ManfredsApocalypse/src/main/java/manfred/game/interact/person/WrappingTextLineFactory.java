package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.util.List;

abstract public class WrappingTextLineFactory {

    protected final GameConfig gameConfig;

    protected WrappingTextLineFactory(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    protected final TextLine wrapTextLinesRecursively(List<GelaberEdge> outgoingEdges, List<String> textLines) {
        int numberOfTextLinesPerBox = gameConfig.getNumberOfTextLines();
        if (textLines.size() <= numberOfTextLinesPerBox) {
            return createFinalTextLine(outgoingEdges, textLines);
        }

        return new TextLineWrapper(
            textLines.subList(0, numberOfTextLinesPerBox),
            this.gameConfig,
            wrapTextLinesRecursively(outgoingEdges, textLines.subList(numberOfTextLinesPerBox - 1, textLines.size()))
        );
    }

    abstract protected TextLine createFinalTextLine(List<GelaberEdge> outgoingEdges, List<String> textLines);
}
