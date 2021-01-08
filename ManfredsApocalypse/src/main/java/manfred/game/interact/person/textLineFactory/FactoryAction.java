package manfred.game.interact.person.textLineFactory;

import manfred.game.interact.person.GelaberEdge;
import manfred.game.interact.person.GelaberNode;
import manfred.game.interact.person.TextLine;

import java.util.List;

public interface FactoryAction {
    TextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges);
}
