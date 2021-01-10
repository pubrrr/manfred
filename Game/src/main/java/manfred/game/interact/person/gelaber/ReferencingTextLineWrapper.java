package manfred.game.interact.person.gelaber;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.interact.person.gelaber.GelaberEdge;
import manfred.game.interact.person.gelaber.GelaberNodeIdentifier;

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
