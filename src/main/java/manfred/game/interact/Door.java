package manfred.game.interact;

import manfred.game.controls.KeyControls;

import java.util.function.Consumer;

public class Door implements Interactable {
    private String destinationName;

    public Door(String destinationName) {
        this.destinationName = destinationName;
    }

    @Override
    public Consumer<KeyControls> interact() {
        return keyControls -> keyControls.loadMap(this.destinationName);
    }
}
