package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChoicesTextLineFactory extends WrappingTextLineFactory implements TextLineFactory {

    public ChoicesTextLineFactory(GameConfig gameConfig) {
        super(gameConfig);
    }

    @Override
    public boolean appliesTo(List<GelaberEdge> outgoingEdges) {
        return outgoingEdges.size() > 1;
    }

    @Override
    public TextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges) {
        return wrapTextLinesRecursively(outgoingEdges, gelaberNode.getTextLines());
    }

    @Override
    protected TextLine createFinalTextLine(List<GelaberEdge> outgoingEdges, List<String> textLines) {
        return new ChoicesText(
            textLines,
            this.gameConfig,
            ChoicesFacade.buildWith(gameConfig).from(outgoingEdges)
        );
    }
}
