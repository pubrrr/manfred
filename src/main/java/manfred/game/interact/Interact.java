package manfred.game.interact;

import manfred.game.controls.KeyControls;

import java.util.function.Consumer;

public interface Interact {
    Consumer<KeyControls> interact();
}
