package manfred.manfreditor.gui.view;

import lombok.AllArgsConstructor;
import manfred.manfreditor.map.Map;
import manfred.manfreditor.map.MapModel;
import manfred.manfreditor.mapobject.MapObject;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.SortedMap;

@Component
@AllArgsConstructor
public class MapView {

    private final MapModel mapModel;
    private final ViewCoordinateMapper viewCoordinateMapper;

    public void draw(GC gc, Display display) {
        SortedMap<Map.TileCoordinate, MapObject> mapObjects = mapModel.getObjects();

        mapModel.getSizeY()
            .toStrictlyPositive()
            .ifPresent(mapSize -> mapObjects.forEach(
                (tileCoordinate, mapObject) -> mapObject.drawAt(
                    viewCoordinateMapper.mapToBottomLeft(tileCoordinate, mapSize),
                    gc,
                    display
                )
            ));
    }

}
