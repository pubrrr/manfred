package manfred.manfreditor.map;

import manfred.data.shared.PositiveInt;
import manfred.manfreditor.mapobject.MapObject;

import java.util.SortedMap;

public class MapModel {
    private Map map;

    public MapModel(Map initialMap) {
        this.map = initialMap;
    }

    public void setMap(Map resultingMap) {
        this.map = resultingMap;
    }

    public SortedMap<Map.TileCoordinate, MapObject> getObjects() {
        return this.map.getObjects();
    }

    public PositiveInt getSizeY() {
        return this.map.getSizeY();
    }
}
