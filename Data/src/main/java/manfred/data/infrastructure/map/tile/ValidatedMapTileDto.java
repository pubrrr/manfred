package manfred.data.infrastructure.map.tile;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import manfred.data.infrastructure.map.MapPrototype;
import org.eclipse.swt.graphics.ImageData;

import java.awt.image.BufferedImage;
import java.util.Comparator;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
public class ValidatedMapTileDto {
    String name;
    MapTileStructurePrototype structure;
    BufferedImage image;
    ImageData imageData;

    /**
     * @return the leftmost, bottommost coordinate of a non accessible tile of this object
     */
    public MapPrototype.Coordinate getOriginCoordinate() {
        return structure.getCoordinateSet().stream()
            .filter(coordinate -> coordinate.getX().value() == 0)
            .filter(coordinate -> !structure.getFromMap(coordinate).isAccessible())
            .min(Comparator.comparingInt(coordinate -> coordinate.getY().value()))
            .orElseGet(structure::getBottomLeftCoordinate);
    }
}
