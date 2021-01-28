package manfred.game.interact.person.gelaber;

import java.awt.*;
import java.util.function.Function;

public interface TextLine {
    void up();

    void down();

    GelaberResponseWrapper next(Function<GelaberNodeIdentifier, TextLine> successorSupplier);

    void paint(Graphics g);
}
