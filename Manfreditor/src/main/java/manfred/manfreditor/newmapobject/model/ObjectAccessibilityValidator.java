package manfred.manfreditor.newmapobject.model;

import io.vavr.collection.Map;
import io.vavr.control.Validation;
import org.springframework.stereotype.Component;

import static manfred.manfreditor.newmapobject.model.NewMapObjectModel.PreviewTileCoordinate;

@Component
public class ObjectAccessibilityValidator {

    public Validation<String, AccessibilityGrid> validate(AccessibilityGrid accessibilityGrid) {
        Map<PreviewTileCoordinate, Boolean> inaccessibleTilesAtX0 = accessibilityGrid.getGrid()
            .filterKeys(previewTileCoordinate -> previewTileCoordinate.x() == 0)
            .filterValues(isAccessible -> !isAccessible);

        if (inaccessibleTilesAtX0.size() == 0) {
            return Validation.invalid("at least one tile in the left column must be inaccessible");
        }

        return Validation.valid(accessibilityGrid);
    }
}
