package manfred.data.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.helper.UrlHelper;
import manfred.data.map.RawMapDto;
import manfred.data.map.TransporterDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class DoorsValidator extends MapObjectDtoValidator<TransporterDto> implements Validator {

    private final UrlHelper urlHelper;

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<TransporterDto> doors = rawMapDto.getDoors();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(doors, urlHelper::getResourceForMap));
        validationMessages.addAll(validateTilesAreNotAccessible(mapMatrix, doors));

        return validationMessages;
    }

    @Override
    protected Function<Map.Entry<String, Optional<URL>>, String> targetNotExistentErrorMessage() {
        return emptyResourceByTarget -> "Resource for door target map " + emptyResourceByTarget.getKey() + " not found";
    }

    @Override
    protected Function<Map.Entry<String, Boolean>, String> accessibilityErrorMessage() {
        return targetEntry -> "Tile for door to " + targetEntry.getKey() + " is accessible";
    }
}
