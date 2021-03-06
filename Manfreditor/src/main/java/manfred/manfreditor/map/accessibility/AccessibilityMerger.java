package manfred.manfreditor.map.accessibility;

import manfred.manfreditor.map.Map;
import manfred.manfreditor.mapobject.MapObject;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AccessibilityMerger {

    public java.util.Map<Map.TileCoordinate, AccessibilityIndicator> merge(java.util.Map<Map.TileCoordinate, MapObject> mapObjects) {
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = initializeEmpty(mapObjects);

        mapObjects.forEach(
            (tileCoordinate, mapObject) -> mapObject.insertAccessibilityIndicatorsAt(tileCoordinate, mergedAccessibility)
        );

        return mergedAccessibility;
    }

    private java.util.Map<Map.TileCoordinate, AccessibilityIndicator> initializeEmpty(java.util.Map<Map.TileCoordinate, MapObject> mapObjects) {
        return mapObjects.keySet()
            .stream()
            .collect(Collectors.toMap(
                tileCoordinate -> tileCoordinate,
                tileCoordinate -> new EmptyAccessibilityIndicator()
            ));
    }
}
