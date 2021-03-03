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

import java.util.Collection;
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
    public Collection<PaintableContainerElement> getPaintableContainerElements() {
        return this.map.getPaintableContainerElements();
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> interactWithTile(Map.TileCoordinate interactionMapTile) {
        return this.map.interactWithTile(interactionMapTile);
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> stepOn(Map.TileCoordinate moveTo) {
        return this.map.stepOn(moveTo);
    }

    @Override
    public boolean isAreaAccessible(Rectangle<Map.Coordinate> area) {
        return this.map.isAreaAccessible(area);
    }

    public Map.TileCoordinate tileAt(PositiveInt tileX, PositiveInt tileY) {
        return this.map.tileAt(tileX, tileY);
    }

    public int getMapSizeX() {
        return this.map.sizeX();
    }

    public int getMapSizeY() {
        return this.map.sizeY();
    }
}
