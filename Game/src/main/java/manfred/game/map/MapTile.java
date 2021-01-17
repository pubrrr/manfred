package manfred.game.map;

import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.Paintable;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.function.Function;

public interface MapTile extends Paintable {

    boolean isAccessible();

    default Function<ManfredController, ControllerInterface> onStep(){
        return ControllerInterface::self;
    }

    default Optional<BufferedImage> getImage() {
        return Optional.empty();
    }
}
