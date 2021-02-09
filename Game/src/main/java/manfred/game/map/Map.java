package manfred.game.map;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.geometry.Rectangle;
import manfred.game.geometry.Vector;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.PaintableContainerElement;

import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

public class Map {
    private final List<List<MapTile>> mapTiles;

    public Map(List<List<MapTile>> mapTiles) {
        this.mapTiles = mapTiles;
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

    private boolean isNotAccessible(TileCoordinate tileCoordinate) {
        return !isAccessible(tileCoordinate.tileX, tileCoordinate.tileY);
    }

    public boolean isAreaAccessible(Rectangle area) {
        TileCoordinate bottomLeftTile = area.getBottomLeft().getTile();
        TileCoordinate topRightTile = area.getTopRight().getTile();

        return IntStream.rangeClosed(bottomLeftTile.tileX, topRightTile.tileX)
            .boxed()
            .flatMap(x -> IntStream.rangeClosed(bottomLeftTile.tileY, topRightTile.tileY).boxed().map(y -> new TileCoordinate(x, y)))
            .filter(this::isNotAccessible)
            .findAny()
            .isEmpty();
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < sizeX()
            && y >= 0 && y < sizeY();
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> interactWithTile(TileCoordinate mapTile) {
        return mapTiles.get(mapTile.tileX).get(mapTile.tileY).interact();
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> stepOn(TileCoordinate mapTile) {
        return mapTiles.get(mapTile.tileX).get(mapTile.tileY).onStep();
    }

    public Stack<PaintableContainerElement> getPaintableContainerElements() {
        Stack<PaintableContainerElement> elements = new Stack<>();

        for (int x = 0; x < sizeX(); x++) {
            for (int y = 0; y < sizeY(); y++) {
                elements.push(new PaintableContainerElement(
                    mapTiles.get(x).get(y),
                    tileAt(PositiveInt.of(x), PositiveInt.of(y)).getTopLeftCoordinate()
                ));
            }
        }

        return elements;
    }

    public Coordinate coordinateAt(int x, int y) {
        return new Coordinate(x, y);
    }

    public Coordinate topRight() {
        return new Coordinate(maxXCoordinate(), maxYCoordinate());
    }

    private int maxYCoordinate() {
        return this.sizeY() * Coordinate.TILE_SIZE - 1;
    }

    private int maxXCoordinate() {
        return this.sizeX() * Coordinate.TILE_SIZE - 1;
    }

    public TileCoordinate tileAt(PositiveInt targetSpawnX, PositiveInt targetSpawnY) {
        int tileX = targetSpawnX.value();
        int tileY = targetSpawnY.value();
        if (tileX >= this.sizeX()) {
            tileX = sizeX() - 1;
        }
        if (tileY >= sizeY()) {
            tileY = sizeY() - 1;
        }

        return new TileCoordinate(tileX, tileY);
    }

    @ToString
    @EqualsAndHashCode
    public class Coordinate {
        private static final int TILE_SIZE = 60;

        private final int x;
        private final int y;

        private Coordinate(int x, int y) {
            if (x > maxXCoordinate()) {
                x = maxXCoordinate();
            }
            if (x < 0) {
                x = 0;
            }
            if (y > maxYCoordinate()) {
                y = maxYCoordinate();
            }
            if (y < 0) {
                y = 0;
            }
            this.x = x;
            this.y = y;
        }

        public Coordinate translate(Vector<Map.Coordinate> tranlsation) {
            return new Coordinate(this.x + tranlsation.x(), this.y + tranlsation.y());
        }

        public Vector<Map.Coordinate> distanceTo(Coordinate other) {
            return Vector.of(other.x - this.x, other.y - this.y);
        }

        public TileCoordinate getTile() {
            return new TileCoordinate(x / TILE_SIZE, y / TILE_SIZE);
        }

        public PanelCoordinate scaleTo(PositiveInt.Strict pixelBlockSize) {
            int invertedY = topRight().y - this.y;

            return new PanelCoordinate(
                this.x * pixelBlockSize.value() / TILE_SIZE,
                invertedY * pixelBlockSize.value() / TILE_SIZE
            );
        }
    }

    @ToString
    @EqualsAndHashCode
    public class TileCoordinate {
        private final int tileX;
        private final int tileY;

        private TileCoordinate(int tileX, int tileY) {
            this.tileX = tileX;
            this.tileY = tileY;
        }

        public Coordinate getBottomLeftCoordinate() {
            return new Coordinate(tileX * Coordinate.TILE_SIZE, tileY * Coordinate.TILE_SIZE);
        }

        public Coordinate getTopLeftCoordinate() {
            return new Coordinate(tileX * Coordinate.TILE_SIZE, tileY * Coordinate.TILE_SIZE + (Coordinate.TILE_SIZE - 1));
        }
    }
}
