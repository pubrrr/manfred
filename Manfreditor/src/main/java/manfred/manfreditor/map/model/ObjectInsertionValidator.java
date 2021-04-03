package manfred.manfreditor.map.model;

import io.vavr.control.Validation;
import manfred.manfreditor.map.model.Map.TileCoordinate;
import manfred.manfreditor.map.model.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Component
public class ObjectInsertionValidator {

    public Validation<List<String>, ConcreteMapObject> mayObjectBeInserted(
        ConcreteMapObject mapObject,
        TileCoordinate tileCoordinate,
        FlattenedMap flattenedMap
    ) {
        return mapObject.getStructureAt(tileCoordinate)
            .toValidation()
            .mapError(List::of)
            .flatMap(tileCoordinateTilePrototypeMap -> {
                List<String> collect = getInvalidTileMessages(flattenedMap, tileCoordinateTilePrototypeMap);
                return collect.isEmpty()
                    ? Validation.valid(mapObject)
                    : Validation.invalid(collect);
            });
    }

    private List<String> getInvalidTileMessages(
        FlattenedMap flattenedMap,
        io.vavr.collection.Map<TileCoordinate, AccessibilityIndicator> tileCoordinateTilePrototypeMap
    ) {
        return tileCoordinateTilePrototypeMap
            .filterNotValues(AccessibilityIndicator::isAccessible)
            .filterNotKeys(tileCoordinate -> flattenedMap.get(tileCoordinate).map(AccessibilityIndicator::isAccessible).getOrElse(true))
            .keySet()
            .map(toErrorMessage(flattenedMap))
            .collect(toList());
    }

    private Function<TileCoordinate, String> toErrorMessage(FlattenedMap flattenedMap) {
        return tileCoordinateOnStructure -> "Tile (" + tileCoordinateOnStructure.getX() + "," + tileCoordinateOnStructure.getY() + ") " +
            "is not accessible, blocked by " + getBlockingObject(flattenedMap, tileCoordinateOnStructure);
    }

    private String getBlockingObject(FlattenedMap flattenedMap, TileCoordinate tileCoordinateOnStructure) {
        return flattenedMap.get(tileCoordinateOnStructure).get()
            .getSource()
            .map(source -> "object " + source.getTileName() + " at " + source.getTileCoordinate().shortRepresentation())
            .orElse("no object");
    }
}
