package manfred.game.interact;

import manfred.game.controls.KeyControls;

import java.awt.*;
import java.util.function.Consumer;

public class Idle implements Interactable {
    @Override
    public Consumer<KeyControls> interact() {
        return KeyControls::doNothing;
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
    }
}
