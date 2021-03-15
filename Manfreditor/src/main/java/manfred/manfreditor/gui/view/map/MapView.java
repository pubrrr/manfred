package manfred.manfreditor.gui.view.map;

import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import manfred.manfreditor.gui.view.GridFilter;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.MapObject;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MapView {

    private final MapModel mapModel;
    private final ViewCoordinateMapper viewCoordinateMapper;

    public void draw(GC gc, Display display) {
        SortedMap<MapViewCoordinate, MapObject> mapObjects = mapModel
            .getObjects()
            .collect(sortingByCoordinates());

        drawAccessibility(gc, display);
        drawGrid(mapObjects, gc);
        drawObjects(mapObjects, gc, display);
    }

    private <T> Collector<Tuple2<Map.TileCoordinate, T>, ?, TreeMap<MapViewCoordinate, T>> sortingByCoordinates() {
        return Collectors.toMap(
            mapObjectByTileCoordinate -> viewCoordinateMapper.mapToBottomLeft(mapObjectByTileCoordinate._1()),
            Tuple2::_2,
            (mapObject, mapObjectWithSameCoordinate) -> mapObject,
            TreeMap::new
        );
    }

    private void drawAccessibility(GC gc, Display display) {
        mapModel.getMergedAccessibility()
            .collect(sortingByCoordinates())
            .forEach((bottomLeft, accessibilityIndicator) -> accessibilityIndicator.indicateAccessibilityAt(bottomLeft, gc, display));
    }

    private void drawGrid(SortedMap<MapViewCoordinate, MapObject> mapObjects, GC gc) {
        mapObjects.forEach(
            (bottomLeft, mapObject) -> gc.drawRectangle(
                bottomLeft.getX(),
                bottomLeft.getY() - TileViewSize.TILE_SIZE,
                TileViewSize.TILE_SIZE,
                TileViewSize.TILE_SIZE
            )
        );
    }

    private void drawObjects(SortedMap<MapViewCoordinate, MapObject> mapObjects, GC gc, Display display) {
        mapObjects.forEach(
            (bottomLeft, mapObject) -> mapObject.drawOnMapAt(bottomLeft, gc, display)
        );
    }

    public Point getMapViewSize() {
        return new Point(
            mapModel.getSizeX().times(TileViewSize.TILE_SIZE).value(),
            mapModel.getSizeY().times(TileViewSize.TILE_SIZE).value()
        );
    }

    public Optional<Map.TileCoordinate> getClickedTile(int x, int y) {
        return mapModel.getObjects()
            .keySet()
            .toJavaStream()
            .map(mapObject -> java.util.Map.entry(mapObject.invertY(), mapObject))
            .filter(GridFilter.tileWithSizeContains(TileViewSize.TILE_SIZE, x, y))
            .findAny()
            .map(java.util.Map.Entry::getValue);
    }
}
