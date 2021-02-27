package manfred.manfreditor.mapobject;

import manfred.manfreditor.gui.view.MapViewCoordinate;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public interface MapObject {

    void drawAt(MapViewCoordinate mapViewCoordinate, GC gc, Display display);

    static MapObject none() {
        return new None();
    }
}
