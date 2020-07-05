package manfred.game.interact;

import manfred.game.controls.KeyControls;
import manfred.game.map.MapTile;

import java.util.function.Consumer;

public interface Interactable extends MapTile {
    Consumer<KeyControls> interact();
}
