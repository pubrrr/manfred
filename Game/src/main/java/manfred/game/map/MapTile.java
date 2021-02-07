package manfred.game.map;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.LocatedPaintable;

public interface MapTile extends LocatedPaintable {

    boolean isAccessible();

    default ControllerStateMapper<ManfredController, ControllerInterface> onStep(){
        return ControllerStateMapper::preserveState;
    }

    default ControllerStateMapper<ManfredController, ControllerInterface> interact() {
        return ControllerStateMapper::preserveState;
    }
}
