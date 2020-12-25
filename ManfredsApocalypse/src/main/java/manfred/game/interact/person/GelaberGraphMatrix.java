package manfred.game.interact.person;

import java.util.List;
import java.util.Map;

public class GelaberGraphMatrix {
    private final Map<GelaberNodeIdentifier, List<GelaberEdge>> graphMatrix;

    public GelaberGraphMatrix(Map<GelaberNodeIdentifier, List<GelaberEdge>> graphMatrix) {
        this.graphMatrix = graphMatrix;
    }

    public List<GelaberEdge> getOutgoingEdgesFor(GelaberNodeIdentifier gelaberNodeIdentifier) {
        return graphMatrix.get(gelaberNodeIdentifier);
    }
}
