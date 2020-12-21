package manfred.game.map;

import manfred.game.attack.AttacksContainer;
import manfred.game.exception.InvalidInputException;
import manfred.game.exception.ManfredException;
import manfred.game.graphics.paintable.PaintablesContainer;
import manfred.game.graphics.paintable.PaintableContainerElement;

import java.io.IOException;
import java.util.Stack;

public class MapWrapper implements PaintablesContainer {
    private final MapReader mapReader;
    private final String initialMapName;
    private final AttacksContainer attacksContainer;

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
            } catch (IOException | ManfredException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void loadMap(String name) throws ManfredException, IOException {
        attacksContainer.clear();
        this.map = mapReader.load(name);
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        return getMap().getPaintableContainerElements();
    }
}
