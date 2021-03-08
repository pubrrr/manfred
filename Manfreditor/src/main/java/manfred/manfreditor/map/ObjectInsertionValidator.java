package manfred.manfreditor.map;

import io.vavr.control.Validation;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import org.springframework.stereotype.Component;
import manfred.data.infrastructure.map.tile.TilePrototype;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class ObjectInsertionValidator {

    public Validation<List<String>, ConcreteMapObject> mayObjectBeInserted(
        ConcreteMapObject mapObject,
        TileCoordinate tileCoordinate,
        java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility
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

    private List<String> getInvalidTileMessages(java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility, java.util.Map<TileCoordinate, TilePrototype> tileCoordinateTilePrototypeMap) {
        return tileCoordinateTilePrototypeMap
            .entrySet()
            .stream()
            .filter(tilePrototypeByCoordinate -> !tilePrototypeByCoordinate.getValue().isAccessible())
            .filter(tilePrototypeByCoordinate -> !mergedAccessibility.get(tilePrototypeByCoordinate.getKey()).isAccessible())
            .map(java.util.Map.Entry::getKey)
            .map(toErrorMessage(mergedAccessibility))
            .collect(toList());
    }

    private Function<TileCoordinate, String> toErrorMessage(java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility) {
        return tileCoordinateOnStructure -> "Tile (" + tileCoordinateOnStructure.getX() + "," + tileCoordinateOnStructure.getY() + ") " +
            "is not accessible, blocked by " + getBlockingObject(mergedAccessibility, tileCoordinateOnStructure);
    }

    private String getBlockingObject(java.util.Map<TileCoordinate, AccessibilityIndicator> mergedAccessibility, TileCoordinate tileCoordinateOnStructure) {
        return mergedAccessibility.get(tileCoordinateOnStructure).getSource()
            .map(source -> "object " + source.getTileName() + " at (" + source.getTileCoordinate().getX() + "," + source.getTileCoordinate().getY() + ")")
            .orElse("no object");
    }
}
