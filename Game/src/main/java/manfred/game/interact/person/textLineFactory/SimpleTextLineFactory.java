package manfred.game.interact.person.textLineFactory;

import manfred.game.config.GameConfig;
import manfred.game.interact.person.gelaber.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SimpleTextLineFactory extends WrappingTextLineFactory implements FactoryAction, FactoryRule {

    private SimpleTextLineFactory(GameConfig gameConfig) {
        super(gameConfig);
    }

    public static FactoryRule withConfig(GameConfig gameConfig) {
        return new SimpleTextLineFactory(gameConfig);
    }

    public Optional<FactoryAction> applicableTo(List<GelaberEdge> outgoingEdges) {
        return outgoingEdges.size() == 1
            ? Optional.of(this)
            : Optional.empty();
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
