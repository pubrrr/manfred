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
        MapMatrix mapMatrix;
        try {
            mapMatrix = buildMapMatrix(rawMap.getMap());
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Could not create map " + rawMap.getName(), e);
        }

        List<String> validationMessages = validators.stream()
            .map(validator -> validator.validate(rawMap))
            .flatMap(Collection::stream)
            .collect(toList());

        if (!validationMessages.isEmpty()) {
            throw new InvalidInputException("Validation of map " + rawMap.getName() + " failed:\n" + String.join(",\n", validationMessages));
        }

        return new ValidatedMapDto(
            rawMap.getName(),
            mapMatrix,
            rawMap.getPersons(),
            rawMap.getPortals(),
            rawMap.getDoors(),
            rawMap.getEnemies()
        );
    }

    private MapMatrix buildMapMatrix(List<String> rawMap) throws InvalidInputException {
        List<List<String>> rawMatrix = rawMap.stream().map(this::splitAtCommas).collect(toList());

        MapMatrix.Builder mapMatrixBuilder = MapMatrix.fromRawData(rawMatrix);
        return mapMatrixBuilder.validateAndBuild();
    }

    private List<String> splitAtCommas(String line) {
        return Arrays.stream(line.split(","))
            .map(String::trim)
            .collect(toList());
    }
}
