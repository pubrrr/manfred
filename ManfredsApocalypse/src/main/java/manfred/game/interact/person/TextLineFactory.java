package manfred.game.interact.person;

import java.util.List;

public interface TextLineFactory {

    boolean appliesTo(List<GelaberEdge> outgoingEdges);

    TextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges);
}
