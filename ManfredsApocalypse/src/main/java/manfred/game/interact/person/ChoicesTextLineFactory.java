package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChoicesTextLineFactory implements TextLineFactory {
    private final GameConfig gameConfig;

    public ChoicesTextLineFactory(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public boolean appliesTo(List<GelaberEdge> outgoingEdges) {
        return outgoingEdges.size() > 1;
    }

    @Override
    public ChoicesText create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges) {
        return new ChoicesText(
            gelaberNode.getTextLines(),
            this.gameConfig,
            ChoicesFacade.buildWith(gameConfig).from(outgoingEdges)
        );
    }
}
