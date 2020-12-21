package manfred.game.interact.person;

import com.google.common.collect.ImmutableBiMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GelaberFacadeBuilder {

    private final List<TextLineFactory> textLineFactories;

    private ImmutableBiMap<GelaberNodeIdentifier, GelaberNode> gelaberNodes;
    private GelaberGraphMatrix gelaberGraphMatrix;

    public GelaberFacadeBuilder(@Qualifier("textLineFactories") List<TextLineFactory> textLineFactories) {
        this.textLineFactories = textLineFactories;
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
