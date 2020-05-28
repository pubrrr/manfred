package manfred.game.interact;

import manfred.game.controls.KeyControls;

import java.util.function.Consumer;

public class Door implements Interactable {
    private String targetName;
    private int targetSpawnX;
    private int targetSpawnY;

    public Door(String targetName, int targetSpawnX, int targetSpawnY) {
        this.targetName = targetName;
        this.targetSpawnX = targetSpawnX;
        this.targetSpawnY = targetSpawnY;
    }

    @Override
    public Consumer<KeyControls> interact() {
        return keyControls -> keyControls.loadMap(this.targetName);
    }
}
