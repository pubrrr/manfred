package manfred.game.map;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.interact.Door;
import manfred.game.interact.person.Person;

import java.awt.*;

@AllArgsConstructor
public class DebugTileWrapper implements MapTile {

    private final MapTile wrapped;
    private final PositiveInt.Strict pixelBlockSize;

    @Override
    public void paint(Graphics g, PanelCoordinate coordinate) {
        g.setColor(Color.BLACK);
        g.drawRect(coordinate.getX(), coordinate.getY(), pixelBlockSize.value(), pixelBlockSize.value());

        if (!wrapped.isAccessible()) {
            if (wrapped instanceof Person || wrapped instanceof Door) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.RED);
            }
            g.fillRect(coordinate.getX(), coordinate.getY(), pixelBlockSize.value(), pixelBlockSize.value());
        }

        wrapped.paint(g, coordinate);
    }

    @Override
    public boolean isAccessible() {
        return wrapped.isAccessible();
    }

    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> onStep() {
        return wrapped.onStep();
    }

    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> interact() {
        return wrapped.interact();
    }
}
