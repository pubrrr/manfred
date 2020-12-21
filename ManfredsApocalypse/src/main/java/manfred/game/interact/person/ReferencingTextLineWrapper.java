package manfred.game.interact.person;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.Optional;
import java.util.function.Function;

public class ReferencingTextLineWrapper implements GelaberEdge {
    private final GelaberNodeIdentifier next;
    private final Function<GelaberController, ControllerInterface> continueCommand;

    ReferencingTextLineWrapper(GelaberNodeIdentifier next, Function<GelaberController, ControllerInterface> continueCommand) {
        this.next = next;
        this.continueCommand = continueCommand;
    }

    public GelaberNodeIdentifier follow() {
        return next;
    }

    public Function<GelaberController, ControllerInterface> getContinueCommand() {
        return continueCommand;
    }
}
