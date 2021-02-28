package manfred.manfreditor.map;

import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public interface AccessibilityIndicator {

    void indicateAccessibilityAt(MapViewCoordinate bottomLeft, GC gc, Display display);
}
