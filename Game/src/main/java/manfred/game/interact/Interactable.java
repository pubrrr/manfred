package manfred.game.interact;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;
import manfred.game.map.MapTile;

import java.util.function.Function;

public interface Interactable extends MapTile {
    Function<ManfredController, ControllerInterface> interact();

    static Idle idle() {
        return new Idle();
    }
}
