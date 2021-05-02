package manfred.data.infrastructure.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.dto.TransporterDto;
import manfred.data.persistence.reader.UrlHelper;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class PortalsValidator extends MapObjectDtoValidator<TransporterDto> implements Validator {

    private final UrlHelper urlHelper;

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<TransporterDto> portals = rawMapDto.getPortals();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(portals, urlHelper::getFileForMap));
        validationMessages.addAll(validateTilesAreAccessible(mapMatrix, portals));

        return validationMessages;
    }

    @Override
    protected Function<Map.Entry<String, File>, String> targetNotExistentErrorMessage() {
        return emptyResourceByTarget -> "Resource for portal target map " + emptyResourceByTarget.getKey() + " not found";
    }

    @Override
    protected Function<Map.Entry<String, Boolean>, String> accessibilityErrorMessage() {
        return targetEntry -> "Tile for portal to " + targetEntry.getKey() + " is not accessible";
    }
}
