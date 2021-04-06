package manfred.manfreditor.map.model;

import io.vavr.control.Validation;
import manfred.data.shared.PositiveInt;
import manfred.manfreditor.common.Memento;
import manfred.manfreditor.map.model.accessibility.AccessibilityMerger;
import manfred.manfreditor.map.model.accessibility.Source;
import manfred.manfreditor.map.model.flattened.FlattenedMap;
import manfred.manfreditor.map.model.mapobject.ConcreteMapObject;
import manfred.manfreditor.map.model.mapobject.MapObject;
import manfred.manfreditor.map.model.mapobject.None;

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

    public io.vavr.collection.Map<Map.TileCoordinate, MapObject> getObjects() {
        return this.map.getObjects();
    }

    public FlattenedMap getFlattenedMap() {
        return new FlattenedMap(
            this.map.getName(),
            this.accessibilityMerger.merge(this.map.getObjects())
        );
    }

    public PositiveInt getSizeY() {
        return this.map.getSizeY();
    }

    public PositiveInt getSizeX() {
        return this.map.getSizeX();
    }

    public Validation<List<String>, ConcreteMapObject> tryInsertObjectAt(ConcreteMapObject mapObject, Map.TileCoordinate tileCoordinate) {
        Validation<List<String>, ConcreteMapObject> result = objectInsertionValidator.mayObjectBeInserted(
            mapObject,
            tileCoordinate,
            getFlattenedMap()
        );

        if (result.isValid()) {
            this.map = this.map.insertObjectAt(mapObject, tileCoordinate);
        }
        return result;
    }

    public void forceInsertObjectAt(LocatedMapObject locatedMapObject) {
        this.map = this.map.insertObjectAt(locatedMapObject.getMapObject(), locatedMapObject.getLocation());
    }

    public Optional<LocatedMapObject> deleteObjectAt(Map.TileCoordinate tileCoordinate) {
        Map.TileCoordinate tileCoordinateToDeleteObjectAt = getFlattenedMap()
            .get(tileCoordinate).get()
            .getSource()
            .map(Source::getTileCoordinate)
            .orElse(tileCoordinate);

        MapObject deletedMapObject = this.map.getObjectAt(tileCoordinateToDeleteObjectAt);
        this.map = this.map.insertObjectAt(MapObject.none(), tileCoordinateToDeleteObjectAt);

        return deletedMapObject instanceof None
            ? Optional.empty()
            : Optional.of(new LocatedMapObject(deletedMapObject, tileCoordinateToDeleteObjectAt));
    }

    @Override
    public Memento<MapModel> backup() {
        return new MapModel(this.map, this.accessibilityMerger, this.objectInsertionValidator);
    }

    @Override
    public void restoreStateOf(MapModel toRestore) {
        toRestore.map = this.map;
    }
}
