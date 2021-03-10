package manfred.manfreditor.mapobject;

import io.vavr.control.Either;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public interface MapObject {

    void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display);

    static MapObject none() {
        return new None();
    }

    Either<String, io.vavr.collection.Map<Map.TileCoordinate, AccessibilityIndicator>> getStructureAt(Map.TileCoordinate objectLocation);
}
