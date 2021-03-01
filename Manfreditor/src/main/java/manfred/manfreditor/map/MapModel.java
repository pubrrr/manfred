package manfred.manfreditor.map;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;

import java.util.List;

public class MapModel {
    private Map map;
    private final AccessibilityMerger accessibilityMerger;

    public MapModel(Map initialMap, AccessibilityMerger accessibilityMerger) {
        this.map = initialMap;
        this.accessibilityMerger = accessibilityMerger;
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

    public List<String> insertObjectAt(ConcreteMapObject mapObject, Map.TileCoordinate tileCoordinate) {
        return null;
    }
}
