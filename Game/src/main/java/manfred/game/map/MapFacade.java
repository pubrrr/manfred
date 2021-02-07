package manfred.game.map;

import manfred.data.InvalidInputException;
import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.graphics.paintable.PaintablesContainer;
import manfred.game.geometry.Rectangle;
import manfred.infrastructureadapter.map.MapProvider;

import java.util.Stack;

public class MapFacade implements PaintablesContainer, CollisionDetector {
    private final MapProvider mapProvider;

    private Map map;

    public MapFacade(MapProvider mapProvider, Map initialMap) {
        this.mapProvider = mapProvider;
        this.map = initialMap;
    }

    public void loadMap(String name, ManfredPositionSetter manfredPositionSetter) throws InvalidInputException {
        this.map = mapProvider.provide(name);
        manfredPositionSetter.resetManfredOnMap(this.map);
    }

    @Override
    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        return this.map.getPaintableContainerElements();
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> interactWithTile(Map.TileCoordinate interactionMapTile) {
        return this.map.interactWithTile(interactionMapTile);
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> stepOn(Map.TileCoordinate moveTo) {
        return this.map.stepOn(moveTo);
    }

    public boolean isAccessible(int x, int y) {
        return this.map.isAccessible(x, y);
    }

    @Override
    public boolean isAreaAccessible(Rectangle area) {
        return this.map.isAreaAccessible(area);
    }

    public Map.TileCoordinate tileAt(PositiveInt tileX, PositiveInt tileY) {
        return this.map.tileAt(tileX, tileY);
    }
}
