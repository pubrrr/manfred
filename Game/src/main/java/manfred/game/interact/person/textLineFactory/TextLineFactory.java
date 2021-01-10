package manfred.game.interact.person.textLineFactory;

import manfred.game.interact.person.gelaber.GelaberEdge;
import manfred.game.interact.person.gelaber.GelaberNode;
import manfred.game.interact.person.gelaber.TextLine;

import java.util.List;

public class TextLineFactory {

    private final FactoryRule rules;

    public TextLineFactory(FactoryRule rules) {
        this.rules = rules;
    }

    public TextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges) {
        return rules.applicableTo(outgoingEdges)
            .map(factoryAction -> factoryAction.create(gelaberNode, outgoingEdges))
            .orElseThrow(() -> new RuntimeException("No matching text line creation strategy found for node " + gelaberNode + " with outgoing edges " + outgoingEdges));
    }
}
