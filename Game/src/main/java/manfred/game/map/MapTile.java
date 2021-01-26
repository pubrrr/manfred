package manfred.game.map;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.Paintable;

public interface MapTile extends Paintable {

    boolean isAccessible();

    default ControllerStateMapper<ManfredController, ControllerInterface> onStep(){
        return ControllerStateMapper::preserveState;
    }
}
