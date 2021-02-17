package manfred.game.map;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.GraphicsAdapter;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.interact.Door;
import manfred.game.interact.Portal;
import manfred.game.interact.person.Person;

import java.awt.*;

@AllArgsConstructor
public class DebugTileWrapper implements MapTile {

    private static final Color TRANSPARENT_RED = new Color(255, 0, 0, 150);
    private final MapTile wrapped;
    private final PositiveInt.Strict pixelBlockSize;

    @Override
    public void paint(GraphicsAdapter g, PanelCoordinate bottomLeftCoordinate) {
        g.setColor(Color.BLACK);
        g.drawRectangle(bottomLeftCoordinate.getX(), bottomLeftCoordinate.getY() - pixelBlockSize.value(), pixelBlockSize.value(), pixelBlockSize.value());

        if (!wrapped.isAccessible()) {
            if (wrapped instanceof Person || wrapped instanceof Door) {
                g.setColor(Color.YELLOW);
            } else if (wrapped instanceof Portal){
                g.setColor(Color.BLUE);
            } else {
                g.setColor(TRANSPARENT_RED);
            }
            g.fillRectangle(bottomLeftCoordinate.getX(), bottomLeftCoordinate.getY() - pixelBlockSize.value(), pixelBlockSize.value(), pixelBlockSize.value());
        }

        wrapped.paint(g, bottomLeftCoordinate);
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
