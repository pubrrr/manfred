package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.game.attack.AttacksContainer;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import manfred.infrastructureadapter.map.MapProvider;

import java.util.Stack;

public class MapFacade implements PaintablesContainer {
    private final MapProvider mapProvider;
    private final String initialMapName;
    private final AttacksContainer attacksContainer;

    private Map map;

    public MapFacade(MapProvider mapProvider, String initialMapName, AttacksContainer attacksContainer) {
        this.mapProvider = mapProvider;
        this.initialMapName = initialMapName;
        this.attacksContainer = attacksContainer;
    }

    public Map getMap() {
        if (map == null) {
            try {
                loadMap(initialMapName);
            } catch (InvalidInputException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public void loadMap(String name) throws InvalidInputException {
        attacksContainer.clear();
        this.map = mapProvider.provide(name);
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        return this.map.getPaintableContainerElements();
    }
}
