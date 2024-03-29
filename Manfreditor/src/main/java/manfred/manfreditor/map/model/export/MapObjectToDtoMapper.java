package manfred.manfreditor.map.model.export;

import io.vavr.collection.Map;
import manfred.data.persistence.dto.RawMapTileDto;
import manfred.manfreditor.newmapobject.model.AccessibilityGrid;
import manfred.manfreditor.newmapobject.model.NewMapObjectData;
import manfred.manfreditor.newmapobject.model.NewMapObjectModel.PreviewTileCoordinate;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

@Component
public class MapObjectToDtoMapper {

    public RawMapTileDto map(NewMapObjectData newMapObjectData) {
        return new RawMapTileDto(
            newMapObjectData.getName(),
            mapStructure(newMapObjectData.getAccessibilityGrid()),
            null,
            newMapObjectData.getImageData()
        );
    }

    private List<String> mapStructure(AccessibilityGrid accessibilityGrid) {
        Map<Integer, ? extends Map<PreviewTileCoordinate, Boolean>> tuple2s = accessibilityGrid.getGrid()
            .groupBy(accessibilityByCoordinate -> accessibilityByCoordinate._1.y());


        return accessibilityGrid.getGrid()
            .groupBy(accessibilityByCoordinate -> accessibilityByCoordinate._1.y())
            .toSortedMap(Comparator.comparingInt(y -> -y), Function.identity())
            .values()
            .map(reduceRowToStringRepresenation())
            .toJavaList();
    }

    private Function<Map<PreviewTileCoordinate, Boolean>, String> reduceRowToStringRepresenation() {
        return accessibilityByCoordinate -> String.join(
            ",",
            accessibilityByCoordinate.toSortedMap(Comparator.comparingInt(PreviewTileCoordinate::x), Function.identity())
                .values()
                .map(accessible -> accessible ? "1" : "0")
        );
    }
}
