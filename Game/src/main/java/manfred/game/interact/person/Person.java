package manfred.game.interact.person;

import manfred.game.characters.sprite.Sprite;
import manfred.game.characters.sprite.SpritePainter;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.interact.person.gelaber.GelaberFacade;
import manfred.game.map.MapTile;

public class Person extends SpritePainter<Sprite> implements MapTile {
    private final String name;
    private final GelaberFacade gelaberFacade;

    public Person(String name, GelaberFacade gelaberFacade, Sprite sprite) {
        super(sprite);
        this.name = name;
        this.gelaberFacade = gelaberFacade;
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
}
