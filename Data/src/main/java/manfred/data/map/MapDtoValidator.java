package manfred.data.map;

import manfred.data.InvalidInputException;
import manfred.data.map.matrix.MapMatrix;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class MapDtoValidator {

    public ValidatedMapDto validate(RawMapDto rawMap) throws InvalidInputException {
        MapMatrix mapMatrix;
        try {
            mapMatrix = buildMapMatrix(rawMap.getMap());
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Could not create map " + rawMap.getName(), e);
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
        return Arrays.asList(line.split(","));
    }
}
