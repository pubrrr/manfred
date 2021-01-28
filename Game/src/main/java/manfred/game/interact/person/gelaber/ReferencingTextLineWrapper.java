package manfred.game.interact.person.gelaber;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.GelaberController;

public class ReferencingTextLineWrapper implements GelaberEdge {
    private final GelaberNodeIdentifier next;
    private final ControllerStateMapper<GelaberController, ControllerInterface> continueCommand;

    private final String edgeText;

    ReferencingTextLineWrapper(GelaberNodeIdentifier next, ControllerStateMapper<GelaberController, ControllerInterface> continueCommand, String edgeText) {
        this.next = next;
        this.continueCommand = continueCommand;
        this.edgeText = edgeText;
    }

    public GelaberNodeIdentifier follow() {
        return next;
    }

    public ControllerStateMapper<GelaberController, ControllerInterface> getContinueCommand() {
        return continueCommand;
    }

    public String getEdgeText() {
        return edgeText;
    }
}
