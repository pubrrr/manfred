package manfred.manfreditor.map.model.accessibility;

import lombok.AllArgsConstructor;
import manfred.manfreditor.map.view.map.MapViewCoordinate;
import manfred.manfreditor.map.view.map.TileViewSize;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.util.Optional;

@AllArgsConstructor
public class ColoredAccessibilityIndicator implements AccessibilityIndicator {

    private final RGB rgb;
    private final Source source;

    @Override
    public void indicateAccessibilityAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
        Color color = new Color(display, this.rgb);
        gc.setBackground(color);
        gc.fillRectangle(bottomLeft.getX(), bottomLeft.getY() - TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE);
        color.dispose();
    }

    @Override
    public boolean isAccessible() {
        return false;
    }

    @Override
    public Optional<Source> getSource() {
        return Optional.of(this.source);
    }
}
