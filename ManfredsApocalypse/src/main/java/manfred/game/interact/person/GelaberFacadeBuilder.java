package manfred.game.interact.person;

import com.google.common.collect.ImmutableBiMap;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class GelaberFacadeBuilder {

    private final List<TextLineFactory> textLineFactories = new LinkedList<>();

    private ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes;
    private GelaberGraphMatrix gelaberGraphMatrix;

    public GelaberFacadeBuilder(SimpleTextLineFactory simpleTextLineFactory) {
        this.textLineFactories.add(simpleTextLineFactory);
    }

    public GelaberFacadeBuilder withNodes(ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes) {
        this.gelaberNodes = gelaberNodes;
        return this;
    }

    public GelaberFacadeBuilder withGraphMatrix(GelaberGraphMatrix gelaberGraphMatrix) {
        this.gelaberGraphMatrix = gelaberGraphMatrix;
        return this;
    }

    public GelaberFacade buildStartingAt(GelaberNodeIdentifier initialGelaberNodeIdentifier) {
        GelaberFacade gelaberFacade = new GelaberFacade(gelaberGraphMatrix, gelaberNodes, textLineFactories, initialGelaberNodeIdentifier);
        reset();
        return gelaberFacade;
    }

    private void reset() {
        this.gelaberNodes = ImmutableBiMap.of();
        this.gelaberGraphMatrix = new GelaberGraphMatrix(Map.of());
    }
}
