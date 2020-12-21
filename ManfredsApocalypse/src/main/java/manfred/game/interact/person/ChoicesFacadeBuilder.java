package manfred.game.interact.person;

import manfred.game.GameConfig;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChoicesFacadeBuilder {
    private final GameConfig gameConfig;

    public ChoicesFacadeBuilder(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public ChoicesFacade from(List<GelaberEdge> outgoingEdges) {
        LinkedHashMap<GelaberNodeIdentifier, GelaberEdge> edgesByIdentifier = outgoingEdges.stream()
            .collect(Collectors.toMap(GelaberEdge::follow, Function.identity(), (edge, sameEdge) -> edge, LinkedHashMap::new));

        return new ChoicesFacade(edgesByIdentifier, Selector.fromEdges(outgoingEdges), gameConfig);
    }
}
