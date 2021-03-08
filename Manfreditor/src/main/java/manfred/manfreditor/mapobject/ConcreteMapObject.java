package manfred.manfreditor.mapobject;

import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.gui.view.map.TileViewSize;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.accessibility.Source;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import io.vavr.control.Either;

import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toMap;

public class ConcreteMapObject implements MapObject {

    private final static RGB red = new RGB(255, 0, 0);

    private final String name;
    private final MapPrototype structure;
    private final MapPrototype.Coordinate originCoordinate;
    private final ImageData imageData;

    public ConcreteMapObject(String name, MapPrototype structure, MapPrototype.Coordinate originCoordinate, ImageData imageData) {
        this.name = name;
        this.structure = structure;
        this.originCoordinate = originCoordinate;
        this.imageData = imageData;
    }

    @Override
    public void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
        Image image = new Image(display, this.imageData);
        gc.drawImage(image, bottomLeft.getX(), bottomLeft.getY() - imageData.height + originCoordinate.getY().value() * TileViewSize.TILE_SIZE);
        image.dispose();
    }

    @Override
    public void insertAccessibilityIndicatorsAt(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        List<MapPrototype.Coordinate> coordinateSet = structure.getCoordinateSet();
        coordinateSet.forEach(overrideNonAccessibleTiles(tileCoordinate, mergedAccessibility));
    }

    private Consumer<MapPrototype.Coordinate> overrideNonAccessibleTiles(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        return coordinate -> {
            TilePrototype tilePrototype = structure.getFromMap(coordinate);
            if (!tilePrototype.isAccessible()) {
                Map.TileCoordinate targetTile = tileCoordinate.translateBy(coordinate).offsetBy(this.originCoordinate);
                var accessibilityIndicator = new ColoredAccessibilityIndicator(red, new Source(this.name, tileCoordinate));

                mergedAccessibility.put(targetTile, accessibilityIndicator);
            }
        };
    }

    public String getName() {
        return this.name;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public Either<String, java.util.Map<Map.TileCoordinate, TilePrototype>> getStructureAt(Map.TileCoordinate objectLocation) {
        if (objectLocation.getY().value() < this.originCoordinate.getY().value()) {
            return Either.left("Object location must not result in negative coordinates, given: " + objectLocation.shortRepresentation()
                + ", origin is " + this.originCoordinate.shortRepresentation());
        }

        return Either.right(
            structure.getCoordinateSet()
                .stream()
                .collect(toMap(
                    coordinate -> objectLocation.translateBy(coordinate).offsetBy(this.originCoordinate),
                    this.structure::getFromMap
                ))
        );
    }
}
