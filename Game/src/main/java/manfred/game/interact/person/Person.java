package manfred.game.interact.person;

import manfred.game.config.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.MapTile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Person implements MapTile {
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

    @Override
    public ControllerStateMapper<ManfredController, ControllerInterface> interact() {
        return controller -> controller.talk(controller, this.gelaberFacade);
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public void paint(Graphics g, Point offset, Integer x, Integer y) {
        g.drawImage(image, x - offset.x, y - offset.y, gameConfig.getPixelBlockSize().value(), gameConfig.getPixelBlockSize().value(), null);
    }
}
