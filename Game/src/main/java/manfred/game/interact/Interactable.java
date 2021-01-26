package manfred.game.interact;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.map.MapTile;

public interface Interactable extends MapTile {
    ControllerStateMapper<ManfredController, ControllerInterface> interact();

    static Idle idle() {
        return new Idle();
    }
}
