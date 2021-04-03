package manfred.manfreditor.map.model.mapobject;

import io.vavr.control.Either;
import manfred.manfreditor.map.view.map.MapViewCoordinate;
import manfred.manfreditor.map.model.Map;
import manfred.manfreditor.map.model.accessibility.AccessibilityIndicator;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public interface MapObject {

    void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display);

    static MapObject none() {
        return new None();
    }

    Either<String, io.vavr.collection.Map<Map.TileCoordinate, AccessibilityIndicator>> getStructureAt(Map.TileCoordinate objectLocation);
}
