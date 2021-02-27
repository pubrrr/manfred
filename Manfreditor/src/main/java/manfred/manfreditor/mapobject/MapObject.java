package manfred.manfreditor.mapobject;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

public interface MapObject {

    void draw(GC gc, Display display);

    static MapObject none() {
        return new None();
    }
}
