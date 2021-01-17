package manfred.game.map;

import manfred.game.config.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.interact.Interactable;

import java.awt.*;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class Map {
    private final List<List<MapTile>> mapTiles;
    private final GameConfig gameConfig;

    public Map(List<List<MapTile>> mapTiles, GameConfig gameConfig) {
        this.mapTiles = mapTiles;
        this.gameConfig = gameConfig;
    }

    public int sizeX() {
        return mapTiles.size();
    }

    public int sizeY() {
        return mapTiles.get(0).size();
    }

    public boolean isAccessible(int x, int y) {
        return isInBounds(x, y) && mapTiles.get(x).get(y).isAccessible();
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < sizeX()
            && y >= 0 && y < sizeY();
    }

    public Interactable getInteractable(Point mapTile) {
        MapTile tile = mapTiles.get(mapTile.x).get(mapTile.y);
        return tile instanceof Interactable
            ? (Interactable) tile
            : Interactable.idle();
    }

    public Function<ManfredController, ControllerInterface> stepOn(Point mapTile) {
        return mapTiles.get(mapTile.x).get(mapTile.y).onStep();
    }

    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        Stack<PaintableContainerElement> elements = new Stack<>();

        for (int x = 0; x < sizeX(); x++) {
            for (int y = 0; y < sizeY(); y++) {
                elements.push(new PaintableContainerElement(
                        mapTiles.get(x).get(y),
                        gameConfig.getPixelBlockSize() * x,
                        gameConfig.getPixelBlockSize() * y
                ));
            }
        }

        return elements;
    }
}
