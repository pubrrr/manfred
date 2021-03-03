package manfred.manfreditor.gui.view;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.MapObject;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MapView {

    private final MapModel mapModel;
    private final ViewCoordinateMapper viewCoordinateMapper;

    public void draw(GC gc, Display display) {
        mapModel.getSizeY()
            .toStrictlyPositive()
            .ifPresent(mapSizeY -> drawMap(gc, display, mapSizeY));
    }

    private void drawMap(GC gc, Display display, PositiveInt.Strict mapSizeY) {
        SortedMap<MapViewCoordinate, MapObject> mapObjects = mapModel.getObjects()
            .entrySet().stream()
            .collect(Collectors.toMap(
                mapObjectByTileCoordinate -> viewCoordinateMapper.mapToBottomLeft(mapObjectByTileCoordinate.getKey(), mapSizeY),
                java.util.Map.Entry::getValue,
                (mapObject, mapObjectWithSameCoordinate) -> mapObject,
                TreeMap::new
            ));

        drawAccessibility(gc, display, mapSizeY);
        drawGrid(mapObjects, gc);
        drawObjects(mapObjects, gc, display);
    }

    private void drawAccessibility(GC gc, Display display, PositiveInt.Strict mapSizeY) {
        mapModel.getMergedAccessibility()
            .entrySet().stream()
            .collect(Collectors.toMap(
                mapObjectByTileCoordinate -> viewCoordinateMapper.mapToBottomLeft(mapObjectByTileCoordinate.getKey(), mapSizeY),
                java.util.Map.Entry::getValue,
                (mapObject, mapObjectWithSameCoordinate) -> mapObject,
                TreeMap::new
            ))
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
            (bottomLeft, mapObject) -> mapObject.drawAt(bottomLeft, gc, display)
        );
    }

    public Point getMapViewSize() {
        return new Point(
            mapModel.getSizeX().times(TileViewSize.TILE_SIZE).value(),
            mapModel.getSizeY().times(TileViewSize.TILE_SIZE).value()
        );
    }
}
