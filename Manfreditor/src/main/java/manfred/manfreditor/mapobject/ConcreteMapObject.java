package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.map.AccessibilityIndicator;
import manfred.manfreditor.map.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.Map;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.util.List;
import java.util.function.Consumer;

public class ConcreteMapObject implements MapObject {

    private final static RGB red = new RGB(255, 0, 0);

    private final String name;
    private final MapPrototype structure;
    private final ImageData imageData;

    public ConcreteMapObject(String name, MapPrototype structure, ImageData imageData) {
        this.name = name;
        this.structure = structure;
        this.imageData = imageData;
    }

    @Override
    public void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
        Image image = new Image(display, this.imageData);
        gc.drawImage(image, bottomLeft.getX(), bottomLeft.getY() - imageData.height);
        image.dispose();
    }

    @Override
    public void insertAccessibilityIndicatorsAt(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        List<MapPrototype.Coordinate> coordinateSet = structure.getCoordinateSet();
        coordinateSet.forEach(overrideNonAccessibleTiles(tileCoordinate, mergedAccessibility));
    }

    private Consumer<MapPrototype.Coordinate> overrideNonAccessibleTiles(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        return coordinate -> {
            Map.TileCoordinate targetTile = tileCoordinate.translateBy(coordinate);
            TilePrototype tilePrototype = structure.getFromMap(coordinate);
            if (!tilePrototype.isAccessible()) {
                mergedAccessibility.put(targetTile, new ColoredAccessibilityIndicator(red));
            }
        };
    }

    public String getName() {
        return this.name;
    }

    public ImageData getImageData() {
        return imageData;
    }
}
