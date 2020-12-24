package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.util.List;

public class ChoicesFacadeBuilder {
    private final GameConfig gameConfig;

    public ChoicesFacadeBuilder(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public ChoicesFacade from(List<GelaberEdge> outgoingEdges) {
        return new ChoicesFacade(outgoingEdges, Selector.fromEdges(outgoingEdges), gameConfig);
    }
}
