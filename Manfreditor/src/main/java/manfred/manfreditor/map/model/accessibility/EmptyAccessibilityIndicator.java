package manfred.manfreditor.map.model.accessibility;

import manfred.manfreditor.map.view.map.MapViewCoordinate;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

import java.util.Optional;

public class EmptyAccessibilityIndicator implements AccessibilityIndicator {
    @Override
    public void indicateAccessibilityAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public Optional<Source> getSource() {
        return Optional.empty();
    }
}
