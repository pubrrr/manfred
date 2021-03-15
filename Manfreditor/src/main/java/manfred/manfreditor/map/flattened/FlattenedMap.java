package manfred.manfreditor.map.flattened;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import manfred.manfreditor.map.Map.TileCoordinate;
import manfred.manfreditor.map.accessibility.AccessibilityIndicator;

public class FlattenedMap {

    private final String name;
    private final Map<TileCoordinate, AccessibilityIndicator> accessibility;

    public FlattenedMap(String name, Map<TileCoordinate, AccessibilityIndicator> accessibility) {
        this.name = name;
        this.accessibility = accessibility;
    }

    public String getName() {
        return name;
    }

    public Map<TileCoordinate, AccessibilityIndicator> getAccessibility() {
        return accessibility;
    }

    public Option<AccessibilityIndicator> get(TileCoordinate tileCoordinate) {
        return accessibility.get(tileCoordinate);
    }
}
