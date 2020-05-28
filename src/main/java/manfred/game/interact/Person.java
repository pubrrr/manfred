package manfred.game.interact;

import manfred.game.controls.KeyControls;
import manfred.game.interact.gelaber.Gelaber;

import java.util.function.Consumer;

public class Person implements Interactable {
    private String name;
    private Gelaber gelaber;

    public Person(String name, Gelaber gelaber){
        this.name = name;
        this.gelaber = gelaber;
    }

    public String getName() {
        return this.name;
    }

    public Gelaber getGelaber() {
        return this.gelaber;
    }

    @Override
    public Consumer<KeyControls> interact() {
        return keyControls -> {
            keyControls.controlGelaber(this.gelaber);
            keyControls.getGamePanel().registerPaintable(this.gelaber);
        };
    }
}
