package manfred.game.interact.person;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.function.Function;

public interface GelaberEdge {

    GelaberNodeIdentifier follow();

    Function<GelaberController, ControllerInterface> getContinueCommand();

    static ReferencingTextLineWrapper continuingWith(GelaberNodeIdentifier next) {
        return new ReferencingTextLineWrapper(next, ControllerInterface::self);
    }

    static ReferencingTextLineWrapper abortingReferencingTo(GelaberNodeIdentifier next) {
        return new ReferencingTextLineWrapper(next, GelaberController::previous);
    }
}
