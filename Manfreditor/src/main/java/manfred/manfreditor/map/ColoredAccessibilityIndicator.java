package manfred.manfreditor.map;

import lombok.AllArgsConstructor;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.gui.view.map.TileViewSize;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

@AllArgsConstructor
public class ColoredAccessibilityIndicator implements AccessibilityIndicator {

    private final RGB rgb;

    @Override
    public void indicateAccessibilityAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
        Color color = new Color(display, this.rgb);
        gc.setBackground(color);
        gc.fillRectangle(bottomLeft.getX(), bottomLeft.getY() - TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE, TileViewSize.TILE_SIZE);
        color.dispose();
    }
}
