package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleTextLineFactory extends WrappingTextLineFactory implements TextLineFactory {

    public SimpleTextLineFactory(GameConfig gameConfig) {
        super(gameConfig);
    }

    public boolean appliesTo(List<GelaberEdge> outgoingEdges) {
        return outgoingEdges.size() == 1;
    }

    @Override
    public TextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges) {
        return wrapTextLinesRecursively(outgoingEdges, gelaberNode.getTextLines());
    }

    @Override
    protected TextLine createFinalTextLine(List<GelaberEdge> outgoingEdges, List<String> textLines) {
        return new SimpleTextLine(textLines, this.gameConfig, outgoingEdges.get(0));
    }
}
