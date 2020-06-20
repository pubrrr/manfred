package manfred.game.interact;

import manfred.game.GameConfig;
import manfred.game.controls.KeyControls;
import manfred.game.interact.gelaber.Gelaber;
import org.springframework.lang.Nullable;

import java.awt.*;
import java.util.function.Consumer;

public class Person implements Interactable {
    private String name;
    private Gelaber gelaber;
    private final GameConfig gameConfig;

    public Person(String name, Gelaber gelaber, GameConfig gameConfig) {
        this.name = name;
        this.gelaber = gelaber;
        this.gameConfig = gameConfig;
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
//            keyControls.getGamePanel().registerPaintableContainer(this.gelaber);
        };
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    @Nullable
    public Consumer<KeyControls> onStep() {
        return null;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.setColor(Color.YELLOW);
        g.fillRect(
                x - offset.x,
                y - offset.y,
                gameConfig.getPixelBlockSize(),
                gameConfig.getPixelBlockSize()
        );
    }
}
