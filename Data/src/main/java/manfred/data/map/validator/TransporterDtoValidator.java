package manfred.data.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.map.MapHelper;
import manfred.data.map.TransporterDto;
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
public abstract class TransporterDtoValidator {

    private final MapHelper mapHelper;

    protected List<String> validateTargetsExist(List<TransporterDto> transporterDtos) {
        return transporterDtos.stream()
            .map(TransporterDto::getTarget)
            .collect(toMapOfTargetMapResourcesByTargetName())
            .entrySet().stream()
            .filter(resourceByTarget -> resourceByTarget.getValue().isEmpty())
            .map(emptyResourceByTarget -> "Resource for target map " + emptyResourceByTarget.getKey() + " not found")
            .collect(Collectors.toList());
    }

    private Collector<String, ?, Map<String, Optional<URL>>> toMapOfTargetMapResourcesByTargetName() {
        return Collectors.toMap(target -> target, mapHelper::getResourceForMap);
    }

    protected List<String> validateAccessibility(MapMatrix<TilePrototype> mapMatrix, List<TransporterDto> transporterDtos, Predicate<Map.Entry<String, Boolean>> accessibilityFilter) {
        return transporterDtos.stream()
            .collect(toMapOfPositionByTargetName())
            .entrySet().stream()
            .map(positionToAccessibilityOnMap(mapMatrix))
            .filter(accessibilityFilter)
            .map(nonAccessiblesByTargetName -> "Tile for " + transporterType() + " to " + nonAccessiblesByTargetName.getKey() + " is not accessible")
            .collect(Collectors.toList());
    }

    protected abstract String transporterType();

    private Function<Map.Entry<String, Point>, Map.Entry<String, Boolean>> positionToAccessibilityOnMap(MapMatrix<TilePrototype> mapMatrix) {
        return positionByTargetName -> Map.entry(positionByTargetName.getKey(), mapMatrix.get(positionByTargetName.getValue().x, positionByTargetName.getValue().y).isAccessible());
    }

    private Collector<TransporterDto, ?, Map<String, Point>> toMapOfPositionByTargetName() {
        return Collectors.toMap(TransporterDto::getTarget, transporterDto -> new Point(transporterDto.getPositionX(), transporterDto.getPositionY()));
    }
}
