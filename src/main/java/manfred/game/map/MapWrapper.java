package manfred.game.map;

import manfred.game.attack.AttacksContainer;
import manfred.game.exception.InvalidInputException;
import manfred.game.graphics.Paintable;

import java.awt.*;
import java.io.IOException;

public class MapWrapper implements Paintable {
    private MapReader mapReader;
    private String initialMapName;
    private AttacksContainer attacksContainer;

    private Map map;

    public MapWrapper(MapReader mapReader, String initialMapName, AttacksContainer attacksContainer) {
        this.mapReader = mapReader;
        this.initialMapName = initialMapName;
        this.attacksContainer = attacksContainer;
    }

    public Map getMap() {
        if (map == null) {
            try {
                loadMap(initialMapName);
            } catch (InvalidInputException | IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void loadMap(String name) throws InvalidInputException, IOException {
        attacksContainer.clear();
        this.map = mapReader.load(name);
    }

    @Override
    public void paint(Graphics g) {
        this.getMap().paint(g);
    }
}
