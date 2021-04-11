package manfred.data.infrastructure.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.persistence.reader.UrlHelper;
import manfred.data.persistence.dto.MapPersonDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class PersonsValidator extends MapObjectDtoValidator<MapPersonDto> implements Validator {

    private final UrlHelper urlHelper;

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<MapPersonDto> persons = rawMapDto.getPersons();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(persons, urlHelper::getFileForPerson));
        validationMessages.addAll(validateTilesAreAccessible(mapMatrix, persons));

        return validationMessages;
    }

    @Override
    protected Function<Map.Entry<String, File>, String> targetNotExistentErrorMessage() {
        return emptyResourceByTarget -> "Resource for person " + emptyResourceByTarget.getKey() + " not found";
    }

    @Override
    protected Function<Map.Entry<String, Boolean>, String> accessibilityErrorMessage() {
        return targetEntry -> "Tile for person " + targetEntry.getKey() + " is not accessible";
    }
}
