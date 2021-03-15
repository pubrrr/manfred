package manfred.manfreditor.map;

import io.vavr.control.Validation;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class ObjectInsertionValidator {

    public Validation<List<String>, ConcreteMapObject> mayObjectBeInserted(
        ConcreteMapObject mapObject,
        TileCoordinate tileCoordinate,
        io.vavr.collection.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility
    ) {
        return mapObject.getStructureAt(tileCoordinate)
            .toValidation()
            .mapError(List::of)
            .flatMap(tileCoordinateTilePrototypeMap -> {
                List<String> collect = getInvalidTileMessages(mergedAccessibility, tileCoordinateTilePrototypeMap);
                return collect.isEmpty()
                    ? Validation.valid(mapObject)
                    : Validation.invalid(collect);
            });
    }

    private List<String> getInvalidTileMessages(
        io.vavr.collection.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility,
        io.vavr.collection.Map<TileCoordinate, AccessibilityIndicator> tileCoordinateTilePrototypeMap
    ) {
        return tileCoordinateTilePrototypeMap
            .filterNotValues(AccessibilityIndicator::isAccessible)
            .filterNotKeys(tileCoordinate -> mergedAccessibility.get(tileCoordinate).map(AccessibilityIndicator::isAccessible).getOrElse(true))
            .keySet()
            .map(toErrorMessage(mergedAccessibility))
            .collect(toList());
    }

    private Function<TileCoordinate, String> toErrorMessage(io.vavr.collection.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        return tileCoordinateOnStructure -> "Tile (" + tileCoordinateOnStructure.getX() + "," + tileCoordinateOnStructure.getY() + ") " +
            "is not accessible, blocked by " + getBlockingObject(mergedAccessibility, tileCoordinateOnStructure);
    }

    private String getBlockingObject(io.vavr.collection.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility, TileCoordinate tileCoordinateOnStructure) {
        return mergedAccessibility.get(tileCoordinateOnStructure).get()
            .getSource()
            .map(source -> "object " + source.getTileName() + " at (" + source.getTileCoordinate().getX() + "," + source.getTileCoordinate().getY() + ")")
            .orElse("no object");
    }
}
