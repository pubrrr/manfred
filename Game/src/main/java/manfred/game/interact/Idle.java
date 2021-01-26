package manfred.game.interact;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;

import java.awt.*;

public class Idle implements Interactable {
    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> interact() {
        return ControllerStateMapper::preserveState;
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
    }
}
