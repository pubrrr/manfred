package manfred.game.interact.person.textLineFactory;

import manfred.game.config.GameConfig;
import manfred.game.interact.person.gelaber.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChoicesTextLineFactory extends WrappingTextLineFactory implements FactoryAction, FactoryRule {

    private ChoicesTextLineFactory(GameConfig gameConfig) {
        super(gameConfig);
    }

    public static FactoryRule withConfig(GameConfig gameConfig) {
        return new ChoicesTextLineFactory(gameConfig);
    }

    @Override
    public Optional<FactoryAction> applicableTo(List<GelaberEdge> outgoingEdges) {
        return outgoingEdges.size() > 1
            ? Optional.of(this)
            : Optional.empty();
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
