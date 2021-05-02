package manfred.manfreditor.map.model.flattened;

import io.vavr.collection.Map;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.MapSource;
import manfred.manfreditor.map.model.Map.TileCoordinate;
import manfred.manfreditor.map.model.accessibility.AccessibilityIndicator;

@AllArgsConstructor
public class FlattenedMap {

    private final String name;
    private final Map<TileCoordinate, AccessibilityIndicator> accessibility;
    private final MapSource mapSource;

    public String getName() {
        return name;
    }

    public Map<TileCoordinate, AccessibilityIndicator> getAccessibility() {
        return accessibility;
    }

    public Option<AccessibilityIndicator> get(TileCoordinate tileCoordinate) {
        return accessibility.get(tileCoordinate);
    }

    public MapSource getMapSource() {
        return mapSource;
    }
}
