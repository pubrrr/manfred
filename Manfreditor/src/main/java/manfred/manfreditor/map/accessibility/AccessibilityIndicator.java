package manfred.manfreditor.map.accessibility;

import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import java.util.Optional;

public interface AccessibilityIndicator {

    void indicateAccessibilityAt(MapViewCoordinate bottomLeft, GC gc, Display display);

    boolean isAccessible();

    Optional<Source> getSource();
}
