package manfred.manfreditor.map;

import manfred.manfreditor.gui.view.MapViewCoordinate;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public class EmptyAccessibilityIndicator implements AccessibilityIndicator {
    @Override
    public void indicateAccessibilityAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
    }
}
