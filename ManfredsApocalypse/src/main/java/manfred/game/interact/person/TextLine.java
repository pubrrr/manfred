package manfred.game.interact.person;

import manfred.game.graphics.paintable.Paintable;

import java.util.function.Function;

public interface TextLine extends Paintable {
    void up();

    void down();

    GelaberResponseWrapper next(Function<GelaberNodeIdentifier, TextLine> successorSupplier);
}
