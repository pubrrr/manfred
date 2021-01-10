package manfred.game.interact.person;

import com.google.common.collect.ImmutableBiMap;
import manfred.game.interact.person.textLineFactory.TextLineFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GelaberFacadeBuilder {

    private final TextLineFactory textLineFactory;

    private ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes;
    private GelaberGraphMatrix gelaberGraphMatrix;

    public GelaberFacadeBuilder(TextLineFactory textLineFactory) {
        this.textLineFactory = textLineFactory;
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
        GelaberFacade gelaberFacade = new GelaberFacade(gelaberGraphMatrix, gelaberNodes, textLineFactory, initialGelaberNodeIdentifier);
        reset();
        return gelaberFacade;
    }

    private void reset() {
        this.gelaberNodes = ImmutableBiMap.of();
        this.gelaberGraphMatrix = new GelaberGraphMatrix(Map.of());
    }
}
