package manfred.manfreditor.mapobject;

import lombok.EqualsAndHashCode;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.Map;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

@EqualsAndHashCode
public class None implements MapObject {

    @Override
    public void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
    }

    @Override
    public void insertAccessibilityIndicatorsAt(Map.TileCoordinate tileCoordinate, java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
    }
}
