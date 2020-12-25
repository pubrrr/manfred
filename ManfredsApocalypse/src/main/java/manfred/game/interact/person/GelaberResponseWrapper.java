package manfred.game.interact.person;

import lombok.Value;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.function.Function;

@Value
public class GelaberResponseWrapper {
    TextLine nextTextLine;
    Function<GelaberController, ControllerInterface> continueCommand;

    public static GelaberResponseWrapper continuingWith(TextLine textLine) {
        return new GelaberResponseWrapper(textLine, ControllerInterface::self);
    }

    public static GelaberResponseWrapper followingTheEdge(GelaberEdge edge, Function<GelaberNodeIdentifier, TextLine> successorSupplier) {
        return new GelaberResponseWrapper(
            successorSupplier.apply(edge.follow()),
            edge.getContinueCommand()
        );
    }
}
