package manfred.manfreditor.map.model.mapobject;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.manfreditor.map.view.map.MapViewCoordinate;
import manfred.manfreditor.map.view.map.TileViewSize;
import manfred.manfreditor.map.model.Map.TileCoordinate;
import manfred.manfreditor.map.model.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.ColoredAccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.EmptyAccessibilityIndicator;
import manfred.manfreditor.map.model.accessibility.Source;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

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

    public String getName() {
        return this.name;
    }

    public ImageData getImageData() {
        return imageData;
    }

    @Override
    public Either<String, Map<TileCoordinate, AccessibilityIndicator>> getStructureAt(TileCoordinate objectLocation) {
        if (objectLocation.getY().value() < this.originCoordinate.getY().value()) {
            return Either.left(
                "Object location must not result in negative coordinates, " +
                    "given: " + objectLocation.shortRepresentation() + ", " +
                    "origin is " + this.originCoordinate.shortRepresentation()
            );
        }

        return Either.right(locateStructureAt(objectLocation));
    }

    private HashMap<TileCoordinate, AccessibilityIndicator> locateStructureAt(TileCoordinate objectLocation) {
        return HashMap
            .ofAll(
                this.structure.getCoordinateSet().stream(),
                coordinate -> objectLocation.translateBy(coordinate).offsetBy(this.originCoordinate),
                structure::getFromMap
            )
            .map((tileCoordinate, tilePrototype) -> Tuple.of(
                tileCoordinate,
                toAccessibilityIndicator(objectLocation, tilePrototype)
            ));
    }

    private AccessibilityIndicator toAccessibilityIndicator(TileCoordinate objectLocation, TilePrototype tilePrototype) {
        return tilePrototype.isAccessible()
            ? new EmptyAccessibilityIndicator()
            : new ColoredAccessibilityIndicator(red, new Source(this.name, objectLocation));
    }
}
