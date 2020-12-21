package manfred.game.interact.person;

import manfred.game.graphics.paintable.Paintable;

import java.util.List;

public interface TextLine extends Paintable {
    void up();

    void down();

    GelaberEdge next();
}
