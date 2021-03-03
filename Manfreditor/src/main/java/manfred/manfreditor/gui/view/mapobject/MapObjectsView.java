package manfred.manfreditor.gui.view.mapobject;

import lombok.AllArgsConstructor;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.gui.view.GridFilter;
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
        Map<ObjectsGridCoordinate, MapObjectRepository.ObjectKey> objectKeysByGridCoordinate = getObjectKeysByGridCoordinate();
        return objectKeysByGridCoordinate.entrySet().stream()
            .filter(GridFilter.tileWithSizeContains(OBJECT_TILE_SIZE, x, y))
            .findAny()
            .map(Map.Entry::getValue);
    }

    public void draw(GC gc, Display display) {
        Map<ObjectsGridCoordinate, MapObjectRepository.ObjectKey> objectKeysByCoordinate = getObjectKeysByGridCoordinate();
        objectKeysByCoordinate.forEach(drawObjectCenterdOnGrid(gc, display));
    }

    private Map<ObjectsGridCoordinate, MapObjectRepository.ObjectKey> getObjectKeysByGridCoordinate() {
        List<MapObjectRepository.ObjectKey> objectKeys = mapObjectRepository.getKeys();
        List<ObjectsGridCoordinate> coordinates = objectsViewCoordinateFactory.getCoordinates(PositiveInt.of(objectKeys.size()));

        Map<ObjectsGridCoordinate, MapObjectRepository.ObjectKey> objectKeysByCoordinate = new HashMap<>();
        for (int i = 0; i < objectKeys.size(); i++) {
            objectKeysByCoordinate.put(
                coordinates.get(i),
                objectKeys.get(i)
            );
        }
        return objectKeysByCoordinate;
    }

    private BiConsumer<ObjectsGridCoordinate, MapObjectRepository.ObjectKey> drawObjectCenterdOnGrid(GC gc, Display display) {
        return (objectsGridCoordinate, objectKey) -> {
            int xOnCanvas = OBJECT_TILE_SIZE * objectsGridCoordinate.getX().value();
            int yOnCanvas = OBJECT_TILE_SIZE * objectsGridCoordinate.getY().value();

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
