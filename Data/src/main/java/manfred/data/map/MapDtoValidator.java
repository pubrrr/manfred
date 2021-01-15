package manfred.data.map;

import manfred.data.InvalidInputException;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.validator.Validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MapDtoValidator {

    private final List<Validator> validators;

    public MapDtoValidator(List<Validator> validators) {
        this.validators = validators;
    }

    public ValidatedMapDto validate(RawMapDto rawMap) throws InvalidInputException {
        MapMatrix mapMatrix = buildMapMatrix(rawMap);

        validateMapObjects(rawMap);

        return new ValidatedMapDto(
            rawMap.getName(),
            mapMatrix,
            rawMap.getPersons(),
            rawMap.getPortals(),
            rawMap.getDoors(),
            rawMap.getEnemies()
        );
    }

    private void validateMapObjects(RawMapDto rawMap) throws InvalidInputException {
        List<String> validationMessages = validators.stream()
            .map(validator -> validator.validate(rawMap))
            .flatMap(Collection::stream)
            .collect(toList());

        if (!validationMessages.isEmpty()) {
            throw new InvalidInputException("Validation of map " + rawMap.getName() + " failed:\n" + String.join(",\n", validationMessages));
        }
    }

    private MapMatrix buildMapMatrix(RawMapDto rawMap) throws InvalidInputException {
        List<List<String>> rawMatrix = rawMap.getMap().stream().map(this::splitAtCommas).collect(toList());

        MapMatrix.Builder mapMatrixBuilder = MapMatrix.fromRawData(rawMatrix);
        try {
            return mapMatrixBuilder.validateAndBuild();
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Could not create map " + rawMap.getName(), e);
        }
    }

    private List<String> splitAtCommas(String line) {
        return Arrays.stream(line.split(","))
            .map(String::trim)
            .collect(toList());
    }
}
