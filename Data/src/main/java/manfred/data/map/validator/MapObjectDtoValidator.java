package manfred.data.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.map.MapObjectDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class MapObjectDtoValidator<V extends MapObjectDto> {

    private static final Predicate<Map.Entry<String, Boolean>> NON_ACCESSIBLE_TILES_INVALID = accessibilityByTargetName -> !accessibilityByTargetName.getValue();
    private static final Predicate<Map.Entry<String, Boolean>> ACCESSIBLE_TILES_INVALID = Map.Entry::getValue;

    protected List<String> validateTargetsExist(List<V> mapObjects, Function<String, Optional<URL>> targetToUrl) {
        return mapObjects.stream()
            .map(MapObjectDto::getTargetToLoad)
            .collect(toMapOfTargetMapResourcesByTargetName(targetToUrl))
            .entrySet().stream()
            .filter(resourceByTarget -> resourceByTarget.getValue().isEmpty())
            .map(targetNotExistentErrorMessage())
            .collect(Collectors.toList());
    }

    private Collector<String, ?, Map<String, Optional<URL>>> toMapOfTargetMapResourcesByTargetName(Function<String, Optional<URL>> targetToUrl) {
        return Collectors.toMap(target -> target, targetToUrl);
    }

    abstract protected Function<Map.Entry<String, Optional<URL>>, String> targetNotExistentErrorMessage();

    protected List<String> validateTilesAreNotAccessible(MapMatrix<TilePrototype> mapMatrix, List<V> mapObjects) {
        return validateAccessibility(mapMatrix, mapObjects, ACCESSIBLE_TILES_INVALID);
    }

    protected List<String> validateTilesAreAccessible(MapMatrix<TilePrototype> mapMatrix, List<V> mapObjects) {
        return validateAccessibility(mapMatrix, mapObjects, NON_ACCESSIBLE_TILES_INVALID);
    }

    private List<String> validateAccessibility(MapMatrix<TilePrototype> mapMatrix, List<V> mapObjects, Predicate<Map.Entry<String, Boolean>> invalidAccessibilityFilter) {
        return mapObjects.stream()
            .collect(toMapOfPositionByTargetName())
            .entrySet().stream()
            .map(positionToAccessibilityOnMap(mapMatrix))
            .filter(invalidAccessibilityFilter)
            .map(accessibilityErrorMessage())
            .collect(Collectors.toList());
    }

    protected abstract Function<Map.Entry<String, Boolean>, String> accessibilityErrorMessage();

    private Function<Map.Entry<String, Point>, Map.Entry<String, Boolean>> positionToAccessibilityOnMap(MapMatrix<TilePrototype> mapMatrix) {
        return positionByTargetName -> Map.entry(positionByTargetName.getKey(), mapMatrix.get(positionByTargetName.getValue().x, positionByTargetName.getValue().y).isAccessible());
    }

    private Collector<MapObjectDto, ?, Map<String, Point>> toMapOfPositionByTargetName() {
        return Collectors.toMap(MapObjectDto::getTargetToLoad, objectDto -> new Point(objectDto.getPositionX(), objectDto.getPositionY()));
    }
}
