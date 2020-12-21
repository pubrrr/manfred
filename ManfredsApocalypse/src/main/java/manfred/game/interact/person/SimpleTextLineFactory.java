package manfred.game.interact.person;

import manfred.game.GameConfig;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SimpleTextLineFactory implements TextLineFactory {

    private final GameConfig gameConfig;

    public SimpleTextLineFactory(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public boolean appliesTo(List<GelaberEdge> outgoingEdges) {
        return outgoingEdges.size() == 1;
    }

    @Override
    public SimpleTextLine create(GelaberNode gelaberNode, List<GelaberEdge> outgoingEdges) {
        return new SimpleTextLine(gelaberNode.getTextLines(), this.gameConfig, outgoingEdges.get(0));
    }
}
