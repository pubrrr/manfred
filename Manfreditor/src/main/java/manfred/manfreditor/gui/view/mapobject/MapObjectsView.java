package manfred.manfreditor.gui.view.mapobject;

import lombok.AllArgsConstructor;
import manfred.manfreditor.gui.view.map.MapViewCoordinate;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MapObjectsView {

    private final MapObjectRepository mapObjectRepository;

    public void draw(GC gc, Display display) {
        List<MapObjectRepository.ObjectKey> objectKeys = mapObjectRepository.getKeys();
        objectKeys.forEach(objectKey -> mapObjectRepository.get(objectKey).drawAt(new MapViewCoordinate(10, 10), gc, display));
    }
}
