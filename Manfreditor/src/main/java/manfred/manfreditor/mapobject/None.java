package manfred.manfreditor.mapobject;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import lombok.EqualsAndHashCode;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;

@EqualsAndHashCode
public class None implements MapObject {

    @Override
    public void drawOnMapAt(MapViewCoordinate bottomLeft, GC gc, Display display) {
    }

    @Override
    public Either<String, Map<TileCoordinate, AccessibilityIndicator>> getStructureAt(TileCoordinate objectLocation) {
        return Either.right(HashMap.empty());
    }
}
