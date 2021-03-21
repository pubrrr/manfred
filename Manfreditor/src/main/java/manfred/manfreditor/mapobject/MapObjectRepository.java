package manfred.manfreditor.mapobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.gui.view.map.TileViewSize;
import org.eclipse.swt.graphics.ImageData;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MapObjectRepository {

    private final Map<ObjectKey, ConcreteMapObject> objectsStorage = new HashMap<>();

    public ConcreteMapObject getOrCreate(ValidatedMapTileDto validatedMapTileDto) {
        return objectsStorage.computeIfAbsent(
            new ObjectKey(validatedMapTileDto.getName()),
            objectKey -> createNewObject(validatedMapTileDto)
        );
    }

    private ConcreteMapObject createNewObject(ValidatedMapTileDto validatedMapTileDto) {
        PositiveInt structureWidth = validatedMapTileDto.getStructure().getSizeX();

        PositiveInt pixelBlockSize = PositiveInt.of(TileViewSize.TILE_SIZE);

        BufferedImage image = validatedMapTileDto.getImage();
        PositiveInt imageWidth = pixelBlockSize.times(structureWidth);
        PositiveInt imageHeight = imageWidth.times(image.getHeight()).divideBy(PositiveInt.ofNonZero(image.getWidth()));

        ImageData imageData = validatedMapTileDto.getImageData().scaledTo(imageWidth.value(), imageHeight.value());
        return new ConcreteMapObject(
            validatedMapTileDto.getName(),
            validatedMapTileDto.getStructure(),
            validatedMapTileDto.getOriginCoordinate(),
            imageData
        );
    }

    public List<ObjectKey> getKeys() {
        return new ArrayList<>(objectsStorage.keySet());
    }

    public ConcreteMapObject get(ObjectKey objectKey) {
        return this.objectsStorage.get(objectKey);
    }

    public ObjectKey populateWith(ValidatedMapTileDto newTileDto) {
        ObjectKey newKey = new ObjectKey(newTileDto.getName());
        objectsStorage.put(newKey, createNewObject(newTileDto));
        return newKey;
    }

    public void delete(ObjectKey objectKey) {
        objectsStorage.remove(objectKey);
    }

    @ToString
    @EqualsAndHashCode
    @Getter
    public static class ObjectKey {
        private final String key;

        private ObjectKey(String key) {
            this.key = key;
        }
    }
}
