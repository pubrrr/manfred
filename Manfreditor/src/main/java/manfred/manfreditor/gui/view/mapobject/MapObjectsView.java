package manfred.manfreditor.gui.view.mapobject;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.mapobject.MapObjectRepository;
import manfred.manfreditor.mapobject.SelectedObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Component
@AllArgsConstructor
public class MapObjectsView {

    public final static int OBJECT_TILE_SIZE = 150;
    public final static PositiveInt.Strict NUMBER_OF_COLUMNS = PositiveInt.ofNonZero(2);
    public final static RGBA CYAN_BACKGROUND = new RGBA(0, 200, 200, 125);

    private final MapObjectRepository mapObjectRepository;
    private final ObjectsViewCoordinateFactory objectsViewCoordinateFactory;
    private final SelectedObject selectedObject;

    public Optional<MapObjectRepository.ObjectKey> getClickedObjectKey(int x, int y) {
        Map<ObjectsViewCoordinate, MapObjectRepository.ObjectKey> objectKeysByGridCoordinate = getObjectKeysByGridCoordinate();
        return objectKeysByGridCoordinate.entrySet().stream()
            .filter(forClickedGrid(x, y))
            .findAny()
            .map(Map.Entry::getValue);
    }

    private Predicate<Map.Entry<ObjectsViewCoordinate, MapObjectRepository.ObjectKey>> forClickedGrid(int x, int y) {
        return objectKeyByGridCoordinate -> {
            ObjectsViewCoordinate objectsViewCoordinate = objectKeyByGridCoordinate.getKey();
            return objectsViewCoordinate.getX() >= x / OBJECT_TILE_SIZE
                && objectsViewCoordinate.getX() < x / OBJECT_TILE_SIZE + 1
                && objectsViewCoordinate.getY() >= y / OBJECT_TILE_SIZE
                && objectsViewCoordinate.getY() < y / OBJECT_TILE_SIZE + 1;
        };
    }

    public void draw(GC gc, Display display) {
        Map<ObjectsViewCoordinate, MapObjectRepository.ObjectKey> objectKeysByCoordinate = getObjectKeysByGridCoordinate();
        objectKeysByCoordinate.forEach(drawObjectCenterdOnGrid(gc, display));
    }

    private Map<ObjectsViewCoordinate, MapObjectRepository.ObjectKey> getObjectKeysByGridCoordinate() {
        List<MapObjectRepository.ObjectKey> objectKeys = mapObjectRepository.getKeys();
        List<ObjectsViewCoordinate> coordinates = objectsViewCoordinateFactory.getCoordinates(PositiveInt.of(objectKeys.size()));

        Map<ObjectsViewCoordinate, MapObjectRepository.ObjectKey> objectKeysByCoordinate = new HashMap<>();
        for (int i = 0; i < objectKeys.size(); i++) {
            objectKeysByCoordinate.put(
                coordinates.get(i),
                objectKeys.get(i)
            );
        }
        return objectKeysByCoordinate;
    }

    private BiConsumer<ObjectsViewCoordinate, MapObjectRepository.ObjectKey> drawObjectCenterdOnGrid(GC gc, Display display) {
        return (objectsViewCoordinate, objectKey) -> {
            int xOnCanvas = OBJECT_TILE_SIZE * objectsViewCoordinate.getX();
            int yOnCanvas = OBJECT_TILE_SIZE * objectsViewCoordinate.getY();

            if (selectedObject.isSelected(objectKey)) {
                Color color = new Color(display, CYAN_BACKGROUND);
                gc.setBackground(color);
                gc.fillRectangle(xOnCanvas, yOnCanvas, OBJECT_TILE_SIZE, OBJECT_TILE_SIZE);
                color.dispose();
            }

            gc.drawRectangle(xOnCanvas, yOnCanvas, OBJECT_TILE_SIZE, OBJECT_TILE_SIZE);

            ImageData scaledImageData = scaleToFitInObjectTile(mapObjectRepository.get(objectKey).getImageData());
            Image image = new Image(display, scaledImageData);
            gc.drawImage(
                image,
                xOnCanvas + (OBJECT_TILE_SIZE - scaledImageData.width) / 2,
                yOnCanvas + (OBJECT_TILE_SIZE - scaledImageData.height) / 2
            );
            image.dispose();
        };
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
