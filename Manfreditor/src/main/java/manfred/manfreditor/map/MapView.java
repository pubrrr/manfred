package manfred.manfreditor.map;

import lombok.AllArgsConstructor;
import manfred.manfreditor.mapobject.MapObject;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.SortedMap;

@Component
@AllArgsConstructor
public class MapView {

    private final MapModel mapModel;

    public void draw(GC gc, Display display) {
        SortedMap<Map.TileCoordinate, MapObject> mapObjects = mapModel.getObjects();
        mapObjects.forEach((tileCoordinate, mapObject) -> mapObject.draw(gc, display));
    }
}
