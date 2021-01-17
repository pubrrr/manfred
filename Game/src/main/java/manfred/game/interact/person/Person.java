package manfred.game.interact.person;

import manfred.game.config.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;
import manfred.game.interact.Interactable;
import manfred.game.interact.person.gelaber.GelaberFacade;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

public class Person implements Interactable {
    private final String name;
    private final GelaberFacade gelaberFacade;
    private final GameConfig gameConfig;
    private final BufferedImage image;

    public Person(String name, GelaberFacade gelaberFacade, GameConfig gameConfig, BufferedImage image) {
        this.name = name;
        this.gelaberFacade = gelaberFacade;
        this.gameConfig = gameConfig;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public GelaberFacade getGelaberFacade() {
        return this.gelaberFacade;
    }

    @Override
    public Function<ManfredController, ControllerInterface> interact() {
        return controller -> controller.talk(controller, this.gelaberFacade);
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
