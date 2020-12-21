package manfred.game.interact.person;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.function.Function;

public interface GelaberEdge {

    GelaberNodeIdentifier follow();

    Function<GelaberController, ControllerInterface> getContinueCommand();

    String getEdgeText();

    static ReferencingTextLineWrapper continuingWith(GelaberNodeIdentifier next, String edgeText) {
        return new ReferencingTextLineWrapper(next, ControllerInterface::self, edgeText);
    }

    static ReferencingTextLineWrapper abortingReferencingTo(GelaberNodeIdentifier next, String edgeText) {
        return new ReferencingTextLineWrapper(next, GelaberController::previous, edgeText);
    }
}
