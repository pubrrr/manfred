package manfred.manfreditor.mapobject;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import manfred.data.infrastructure.map.tile.ValidatedMapTileDto;
import manfred.data.shared.PositiveInt;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int structureWidth = validatedMapTileDto.getStructure().sizeX();

        PositiveInt pixelBlockSize = PositiveInt.of(100); // TODO...

        BufferedImage image = validatedMapTileDto.getImage();
        PositiveInt imageWidth = pixelBlockSize.times(structureWidth);
        PositiveInt imageHeight = imageWidth.times(image.getHeight()).divideBy(PositiveInt.ofNonZero(image.getWidth()));

        return new ConcreteMapObject(
            validatedMapTileDto.getName(),
            validatedMapTileDto.getStructure(),
            new Sprite(imageHeight, imageWidth, image)
        );
    }

    public List<ObjectKey> getKeys() {
        return new ArrayList<>(objectsStorage.keySet());
    }

    public ConcreteMapObject get(ObjectKey objectKey) {
        return this.objectsStorage.get(objectKey);
    }

    public void populateWith(ValidatedMapTileDto newTileDto) {
        // TODO
    }

    @ToString
    @EqualsAndHashCode
    public static class ObjectKey {
        private final String key;

        private ObjectKey(String key) {
            this.key = key;
        }
    }
}
