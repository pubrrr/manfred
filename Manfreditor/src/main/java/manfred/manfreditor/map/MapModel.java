package manfred.manfreditor.map;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.map.ObjectInsertionValidator.Result;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.accessibility.AccessibilityMerger;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;

import java.util.List;

public class MapModel {

    private Map map;
    private final AccessibilityMerger accessibilityMerger;
    private final ObjectInsertionValidator objectInsertionValidator;

    public MapModel(Map initialMap, AccessibilityMerger accessibilityMerger, ObjectInsertionValidator objectInsertionValidator) {
        this.map = initialMap;
        this.accessibilityMerger = accessibilityMerger;
        this.objectInsertionValidator = objectInsertionValidator;
    }

    public void setMap(Map resultingMap) {
        this.map = resultingMap;
    }

    public java.util.Map<Map.TileCoordinate, MapObject> getObjects() {
        return this.map.getObjects();
    }

    public java.util.Map<Map.TileCoordinate, AccessibilityIndicator> getMergedAccessibility() {
        return this.accessibilityMerger.merge(this.map.getObjects());
    }

    public PositiveInt getSizeY() {
        return this.map.getSizeY();
    }

    public PositiveInt getSizeX() {
        return this.map.getSizeX();
    }

    public List<String> tryInsertObjectAt(ConcreteMapObject mapObject, Map.TileCoordinate tileCoordinate) {
        java.util.Map<Map.TileCoordinate, AccessibilityIndicator> mergedAccessibility = getMergedAccessibility();

        Result result = objectInsertionValidator.mayObjectBeInserted(mapObject, tileCoordinate, mergedAccessibility);

        if (result.wasSuccessful()) {
            this.map.insertObjectAt(mapObject, tileCoordinate);
        }
        return result.getValidationMessages();
    }
}
