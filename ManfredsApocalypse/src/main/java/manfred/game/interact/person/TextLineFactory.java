package manfred.game.interact.person;

import java.util.List;

public interface TextLineFactory {

    boolean appliesTo(List<ReferencingTextLineWrapper> outgoingEdges);

    TextLine create(GelaberNode gelaberNode, List<ReferencingTextLineWrapper> outgoingEdges);
}
