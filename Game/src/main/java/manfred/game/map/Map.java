package manfred.game.map;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.shared.PositiveInt;
import manfred.game.controls.ControllerInterface;
import manfred.game.controls.ControllerStateMapper;
import manfred.game.controls.ManfredController;
import manfred.game.geometry.Rectangle;
import manfred.game.geometry.Vector;
import manfred.game.graphics.PanelCoordinate;
import manfred.game.graphics.paintable.PaintableContainerElement;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Map {
    private final java.util.Map<TileCoordinate, MapTile> mapTiles;
    private final int sizeX;
    private final int sizeY;

    public Map(java.util.Map<MapPrototype.Coordinate, MapTile> mapTiles) {
        this.mapTiles = mapTiles.entrySet()
            .stream()
            .collect(Collectors.toMap(
                mapTileByCoordinate -> new TileCoordinate(mapTileByCoordinate.getKey()),
                java.util.Map.Entry::getValue
            ));
        this.sizeX = this.mapTiles.keySet().stream().map(TileCoordinate::getTileX).reduce(0, Math::max) + 1;
        this.sizeY = this.mapTiles.keySet().stream().map(TileCoordinate::getTileY).reduce(0, Math::max) + 1;
    }

    public int sizeX() {
        return this.sizeX;
    }

    public int sizeY() {
        return this.sizeY;
    }

    private boolean isNotAccessible(TileCoordinate tileCoordinate) {
        return !mapTiles.get(tileCoordinate).isAccessible();
    }

    public boolean isAreaAccessible(Rectangle<Coordinate> area) {
        TileCoordinate bottomLeftTile = area.getBottomLeft().getTile();
        TileCoordinate topRightTile = area.getTopRight().getTile();

        return IntStream.rangeClosed(bottomLeftTile.tileX, topRightTile.tileX)
            .boxed()
            .flatMap(x -> IntStream.rangeClosed(bottomLeftTile.tileY, topRightTile.tileY).boxed().map(y -> new TileCoordinate(x, y)))
            .filter(this::isNotAccessible)
            .findAny()
            .isEmpty();
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> interactWithTile(TileCoordinate mapTile) {
        return mapTiles.get(mapTile).interact();
    }

    public ControllerStateMapper<ManfredController, ControllerInterface> stepOn(TileCoordinate mapTile) {
        return mapTiles.get(mapTile).onStep();
    }

    public Collection<PaintableContainerElement> getPaintableContainerElements() {
        return this.mapTiles.entrySet().stream()
            .map(tileByCoordinate -> new PaintableContainerElement(
                tileByCoordinate.getValue(),
                tileByCoordinate.getKey().getBottomLeftCoordinate()
            ))
            .collect(Collectors.toList());
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
    public class Coordinate implements manfred.game.geometry.Coordinate<Coordinate> {
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

        @Override
        public Coordinate translate(Vector<Map.Coordinate> tranlsation) {
            return new Coordinate(this.x + tranlsation.x(), this.y + tranlsation.y());
        }

        @Override
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

        private TileCoordinate(MapPrototype.Coordinate tileCoordinatePrototype) {
            this(tileCoordinatePrototype.getX().value(), tileCoordinatePrototype.getY().value());
        }

        private int getTileX() {
            return tileX;
        }

        private int getTileY() {
            return tileY;
        }

        public Coordinate getBottomLeftCoordinate() {
            return new Coordinate(tileX * Coordinate.TILE_SIZE, tileY * Coordinate.TILE_SIZE);
        }
    }
}
