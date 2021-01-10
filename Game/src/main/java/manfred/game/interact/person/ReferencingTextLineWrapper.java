package manfred.game.interact.person;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.function.Function;

public class ReferencingTextLineWrapper implements GelaberEdge {
    private final GelaberNodeIdentifier next;
    private final Function<GelaberController, ControllerInterface> continueCommand;

    private final String edgeText;

    ReferencingTextLineWrapper(GelaberNodeIdentifier next, Function<GelaberController, ControllerInterface> continueCommand, String edgeText) {
        this.next = next;
        this.continueCommand = continueCommand;
        this.edgeText = edgeText;
    }

    public GelaberNodeIdentifier follow() {
        return next;
    }

    public Function<GelaberController, ControllerInterface> getContinueCommand() {
        return continueCommand;
    }

    public String getEdgeText() {
        return edgeText;
    }
}
