package manfred.manfreditor.mapobject;

import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.map.AccessibilityIndicator;
import manfred.manfreditor.map.Map;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class None implements MapObject {

    @Override
    public void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
    }

    @Override
    public void insertAccessibilityIndicatorsAt(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
    }
}
