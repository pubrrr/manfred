package manfred.manfreditor.map.model.accessibility;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.control.Either;
import manfred.manfreditor.map.model.Map.TileCoordinate;
import manfred.manfreditor.map.model.mapobject.MapObject;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@Component
public class AccessibilityMerger {

    public Map<TileCoordinate, AccessibilityIndicator> merge(Map<TileCoordinate, MapObject> mapObjects) {
        Map<TileCoordinate, AccessibilityIndicator> mergedObjects = mapObjects
            .map((tileCoordinate, mapObject) -> Tuple.of(tileCoordinate, mapObject.getStructureAt(tileCoordinate)))
            .filterValues(Either::isRight)
            .mapValues(Either::get)
            .values()
            .fold(HashMap.empty(), mergeStructureMaps())
            .filterKeys(mapObjects::containsKey);

        return mapObjects
            .keySet()
            .toMap(tileCoordinate -> Tuple.of(
                tileCoordinate,
                mergedObjects.get(tileCoordinate).getOrElse(new EmptyAccessibilityIndicator())
            ));
    }

    private BiFunction<Map<TileCoordinate, AccessibilityIndicator>, Map<TileCoordinate, AccessibilityIndicator>, Map<TileCoordinate, AccessibilityIndicator>> mergeStructureMaps() {
        return (firstObjectStructure, secondObjectStructure) -> firstObjectStructure.merge(
            secondObjectStructure,
            mergeKeepingNonAccessibles()
        );
    }

    private BiFunction<AccessibilityIndicator, AccessibilityIndicator, AccessibilityIndicator> mergeKeepingNonAccessibles() {
        return (indicator, otherIndicator) -> !indicator.isAccessible() ? indicator : otherIndicator;
    }
}
