package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import manfred.game.interact.Interactable;
import manfred.infrastructureadapter.map.MapProvider;

import java.awt.*;
import java.util.Stack;

public class MapFacade implements PaintablesContainer {
    private final MapProvider mapProvider;

    private Map map;

    public MapFacade(MapProvider mapProvider, Map initialMap) {
        this.mapProvider = mapProvider;
        this.map = initialMap;
    }

    public void loadMap(String name) throws InvalidInputException {
        this.map = mapProvider.provide(name);
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        return this.map.getPaintableContainerElements();
    }

    public Interactable getInteractable(Point interactionMapTile) {
        return this.map.getInteractable(interactionMapTile);
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> stepOn(Point moveTo) {
        return this.map.stepOn(moveTo);
    }

    public int getMapSizeX() {
        return this.map.sizeX();
    }

    public int getMapSizeY() {
        return this.map.sizeY();
    }

    public boolean isAccessible(int x, int y) {
        return this.map.isAccessible(x, y);
    }
}
