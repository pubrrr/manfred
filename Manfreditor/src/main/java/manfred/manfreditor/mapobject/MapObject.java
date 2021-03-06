package manfred.manfreditor.mapobject;

import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.Map;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public interface MapObject {

    void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display);

    static MapObject none() {
        return new None();
    }

    void insertAccessibilityIndicatorsAt(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility);
}
