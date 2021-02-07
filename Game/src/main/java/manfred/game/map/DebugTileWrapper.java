package manfred.game.map;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;

import java.awt.*;

@AllArgsConstructor
public class DebugTileWrapper implements MapTile {

    private final MapTile wrapped;
    private final PositiveInt.Strict pixelBlockSize;

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, pixelBlockSize.value(), pixelBlockSize.value());

        if (!wrapped.isAccessible()) {
            g.setColor(Color.RED);
            g.fillRect(x, y, pixelBlockSize.value(), pixelBlockSize.value());
        }

        wrapped.paint(g, offset, x, y);
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
