package manfred.game.interact.person.textLineFactory;

import manfred.game.interact.person.gelaber.GelaberEdge;
import manfred.game.interact.person.gelaber.GelaberNode;
import manfred.game.interact.person.gelaber.TextLine;

import java.util.List;

public interface FactoryAction {
    TextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges);
}
