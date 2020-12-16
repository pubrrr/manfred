package manfred.game.interact;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;

import java.awt.*;
import java.util.function.Function;

public class Idle implements Interactable {
    @Override
    public Function<ManfredController, ControllerInterface> interact() {
        return ControllerInterface::self;
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
    }
}
