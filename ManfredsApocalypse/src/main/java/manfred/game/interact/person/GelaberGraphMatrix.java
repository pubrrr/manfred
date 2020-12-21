package manfred.game.interact.person;

import java.util.List;
import java.util.Map;

public class GelaberGraphMatrix {
    private final Map<GelaberNodeIdentifier, List<ReferencingTextLineWrapper>> graphMatrix;

    public GelaberGraphMatrix(Map<GelaberNodeIdentifier, List<ReferencingTextLineWrapper>> graphMatrix) {
        this.graphMatrix = graphMatrix;
    }

    public List<ReferencingTextLineWrapper> getOutgoingEdgesFor(GelaberNodeIdentifier gelaberNodeIdentifier) {
        return graphMatrix.get(gelaberNodeIdentifier);
    }
}
