package manfred.game.interact;

import manfred.game.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.GelaberController;
import manfred.game.controls.ManfredController;
import manfred.game.interact.gelaber.Gelaber;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

public class Person implements Interactable {
    private final String name;
    private final Gelaber gelaber;
    private final GameConfig gameConfig;
    private final BufferedImage image;

    public Person(String name, Gelaber gelaber, GameConfig gameConfig, BufferedImage image) {
        this.name = name;
        this.gelaber = gelaber;
        this.gameConfig = gameConfig;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public Gelaber getGelaber() {
        return this.gelaber;
    }

    @Override
    public Function<ManfredController, ControllerInterface> interact() {
        return controller -> {
            controller.stop();
            return new GelaberController(controller, this.gelaber);
        };
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.drawImage(image, x - offset.x, y - offset.y, gameConfig.getPixelBlockSize(), gameConfig.getPixelBlockSize(), null);
    }
}
