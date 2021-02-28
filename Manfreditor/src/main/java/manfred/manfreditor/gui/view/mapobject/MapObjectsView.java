package manfred.manfreditor.gui.view.mapobject;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObjectRepository;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class MapObjectsView {

    public final static int OBJECT_TILE_SIZE = 150;
    public final static PositiveInt.Strict NUMBER_OF_COLUMNS = PositiveInt.ofNonZero(2);

    private final MapObjectRepository mapObjectRepository;
    private final ObjectsViewCoordinateFactory objectsViewCoordinateFactory;

    public void draw(GC gc, Display display) {
        List<MapObjectRepository.ObjectKey> objectKeys = mapObjectRepository.getKeys();
        List<ObjectsViewCoordinate> coordinates = objectsViewCoordinateFactory.getCoordinates(PositiveInt.of(objectKeys.size()));

        Map<ObjectsViewCoordinate, ConcreteMapObject> objectsByCoordinate = new HashMap<>();
        for (int i = 0; i < objectKeys.size(); i++) {
            objectsByCoordinate.put(
                coordinates.get(i),
                mapObjectRepository.get(objectKeys.get(i))
            );
        }

        objectsByCoordinate.forEach((objectsViewCoordinate, concreteMapObject) -> {
            int xOnCanvas = OBJECT_TILE_SIZE * objectsViewCoordinate.getX();
            int yOnCanvas = OBJECT_TILE_SIZE * objectsViewCoordinate.getY();

            gc.drawRectangle(xOnCanvas, yOnCanvas, OBJECT_TILE_SIZE, OBJECT_TILE_SIZE);

            ImageData scaledImageData = scaleToFitInObjectTile(concreteMapObject.getImageData());
            Image image = new Image(display, scaledImageData);
            gc.drawImage(
                image,
                xOnCanvas + (OBJECT_TILE_SIZE - scaledImageData.width) / 2,
                yOnCanvas + (OBJECT_TILE_SIZE - scaledImageData.height) / 2
            );
            image.dispose();
        });
    }

    private ImageData scaleToFitInObjectTile(ImageData imageData) {
        if (imageData.width > imageData.height) {
            return imageData.scaledTo(
                OBJECT_TILE_SIZE,
                imageData.height * OBJECT_TILE_SIZE / imageData.width
            );
        } else {
            return imageData.scaledTo(
                imageData.width * OBJECT_TILE_SIZE / imageData.height,
                OBJECT_TILE_SIZE
            );
        }
    }
}
