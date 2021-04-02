package manfred.manfreditor.mapobject;

import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import manfred.manfreditor.mapobject.NewMapObjectModel.PreviewTileCoordinate;

@AllArgsConstructor
@EqualsAndHashCode
public class AccessibilityGrid {

    private final Map<PreviewTileCoordinate, Boolean> accessibilityGrid;

    public int getSizeX() {
        return accessibilityGrid.keySet()
            .map(PreviewTileCoordinate::x)
            .max()
            .getOrElse(0) + 1;
    }

    public int getSizeY() {
        return accessibilityGrid.keySet()
            .map(PreviewTileCoordinate::y)
            .max()
            .getOrElse(0) + 1;
    }

    public Map<PreviewTileCoordinate, Boolean> getGrid() {
        return this.accessibilityGrid;
    }
}
