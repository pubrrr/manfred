package manfred.data.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.map.MapHelper;
import manfred.data.map.RawMapDto;
import manfred.data.map.TransporterDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.awt.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PortalsValidator implements Validator {

    private final MapHelper mapHelper;

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<TransporterDto> portals = rawMapDto.getPortals();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(portals));
        validationMessages.addAll(validatePortalIsAccessible(mapMatrix, portals));

        return validationMessages;
    }

    private List<String> validateTargetsExist(List<TransporterDto> portals) {
        return portals.stream()
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

    private List<String> validatePortalIsAccessible(MapMatrix<TilePrototype> mapMatrix, List<TransporterDto> portals) {
        return portals.stream()
            .collect(toMapOfPositionByTargetName())
            .entrySet().stream()
            .map(positionToAccessibilityOnMap(mapMatrix))
            .filter(this::notAccessibleTiles)
            .map(nonAccessiblesByTargetName -> "Tile for portal to " + nonAccessiblesByTargetName.getKey() + " is not accessible")
            .collect(Collectors.toList());
    }

    private Function<Map.Entry<String, Point>, Map.Entry<String, Boolean>> positionToAccessibilityOnMap(MapMatrix<TilePrototype> mapMatrix) {
        return positionByTargetName -> Map.entry(positionByTargetName.getKey(), mapMatrix.get(positionByTargetName.getValue().x, positionByTargetName.getValue().y).isAccessible());
    }

    private Collector<TransporterDto, ?, Map<String, Point>> toMapOfPositionByTargetName() {
        return Collectors.toMap(TransporterDto::getTarget, transporterDto -> new Point(transporterDto.getPositionX(), transporterDto.getPositionY()));
    }

    private boolean notAccessibleTiles(Map.Entry<String, Boolean> accessibilityByTargetName) {
        return !accessibilityByTargetName.getValue();
    }
}
