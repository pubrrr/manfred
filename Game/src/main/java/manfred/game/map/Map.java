package manfred.game.map;

import manfred.game.config.GameConfig;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.graphics.paintable.PaintableContainerElement;
import manfred.game.interact.Interactable;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

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

    public ControllerStateMapper<ManfredController, ControllerInterface> stepOn(Point mapTile) {
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

    public Coordinate coordinateAt(int x, int y) {
        Coordinate bottomRight = topRight();
        if (x > bottomRight.x) {
            x = bottomRight.x;
        }
        if (x < 0) {
            x = 0;
        }
        if (y > bottomRight.y) {
            y = bottomRight.y;
        }
        if (y < 0) {
            y = 0;
        }
        return new Coordinate(x, y);
    }

    public Coordinate topRight() {
        return new Coordinate(
            this.sizeX() * Coordinate.TILE_SIZE - 1,
            this.sizeY() * Coordinate.TILE_SIZE - 1
        );
    }

    public static class Coordinate {
        private static final int TILE_SIZE = 100;

        private final int x;
        private final int y;

        private Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate translate(Vector vector) {
            return new Coordinate(this.x + vector.x(), this.y + vector.y());
        }

        public Vector distanceTo(Coordinate other) {
            return Vector.of(other.x - this.x, other.y - this.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Coordinate that = (Coordinate) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
