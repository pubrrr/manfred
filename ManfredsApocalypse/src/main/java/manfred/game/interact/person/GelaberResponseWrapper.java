package manfred.game.interact.person;

import lombok.Value;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;

import java.util.function.Function;

@Value
public class GelaberResponseWrapper {
    TextLine nextTextLine;
    Function<GelaberController, ControllerInterface> continueCommand;
}
