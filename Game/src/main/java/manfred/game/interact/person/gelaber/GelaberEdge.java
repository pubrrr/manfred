package manfred.game.interact.person.gelaber;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.GelaberController;

public interface GelaberEdge {

    GelaberNodeIdentifier follow();

    ControllerStateMapper<GelaberController, ControllerInterface> getContinueCommand();

    String getEdgeText();

    static ReferencingTextLineWrapper continuingWith(GelaberNodeIdentifier next, String edgeText) {
        return new ReferencingTextLineWrapper(next, ControllerStateMapper::preserveState, edgeText);
    }

    static ReferencingTextLineWrapper abortingReferencingTo(GelaberNodeIdentifier next, String edgeText) {
        return new ReferencingTextLineWrapper(next, GelaberController::previous, edgeText);
    }
}
