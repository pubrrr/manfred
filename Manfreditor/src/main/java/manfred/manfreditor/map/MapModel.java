package manfred.manfreditor.map;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.common.Memento;
import manfred.manfreditor.map.ObjectInsertionValidator.Result;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;
import manfred.manfreditor.map.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.accessibility.Source;
import manfred.manfreditor.mapobject.ConcreteMapObject;
import manfred.manfreditor.mapobject.MapObject;
import manfred.manfreditor.mapobject.None;

import java.util.List;
import java.util.Optional;

public class MapModel implements Memento<MapModel> {

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

    public void forceInsertObjectAt(LocatedMapObject locatedMapObject) {
        this.map.insertObjectAt(locatedMapObject.getMapObject(), locatedMapObject.getLocation());
    }

    public Optional<LocatedMapObject> deleteObjectAt(Map.TileCoordinate tileCoordinate) {
        Map.TileCoordinate tileCoordinateToDeleteObjectAt = getMergedAccessibility()
            .get(tileCoordinate)
            .getSource()
            .map(Source::getTileCoordinate)
            .orElse(tileCoordinate);

        MapObject deletedMapObject = this.map.getObjectAt(tileCoordinateToDeleteObjectAt);
        this.map.insertObjectAt(MapObject.none(), tileCoordinateToDeleteObjectAt);

        return deletedMapObject instanceof None
            ? Optional.empty()
            : Optional.of(new LocatedMapObject(deletedMapObject, tileCoordinateToDeleteObjectAt));
    }

    @Override
    public Memento<MapModel> backup() {
        return new MapModel(this.map, this.accessibilityMerger, this.objectInsertionValidator);
    }

    @Override
    public void restoreStateOf(MapModel backup) {
        backup.map = this.map;
    }
}
