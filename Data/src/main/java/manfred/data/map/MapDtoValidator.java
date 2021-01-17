package manfred.data.map;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.enemy.EnemyDto;
import manfred.data.enemy.EnemyReader;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TileConverter;
import manfred.data.map.tile.TilePrototype;
import manfred.data.map.validator.Validator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static manfred.data.helper.StringSplitter.splitAtCommas;

@AllArgsConstructor
public class MapDtoValidator {

    private final List<Validator> validators;
    private final TileConverter tileConverter;
    private final EnemyReader enemyReader;

    public ValidatedMapDto validate(RawMapDto rawMap) throws InvalidInputException {
        // TODO validate that map tile image interferes with other images on the map
        MapMatrix<TilePrototype> mapMatrix = buildMapMatrix(rawMap);

        // TODO properly implement validators
        validateMapObjects(rawMap);

        return new ValidatedMapDto(
            rawMap.getName(),
            mapMatrix,
            rawMap.getPersons(),
            rawMap.getPortals(),
            rawMap.getDoors(),
            validateAndLocateEnemies(rawMap)
        );
    }

    private void validateMapObjects(RawMapDto rawMap) throws InvalidInputException {
        List<String> validationMessages = this.validators.stream()
            .map(validator -> validator.validate(rawMap))
            .flatMap(Collection::stream)
            .collect(toList());

        if (!validationMessages.isEmpty()) {
            throw new InvalidInputException("Validation of map " + rawMap.getName() + " failed:\n" + String.join(",\n", validationMessages));
        }
    }

    private MapMatrix<TilePrototype> buildMapMatrix(RawMapDto rawMap) throws InvalidInputException {
        MapMatrix.Builder<String> mapMatrixBuilder = MapMatrix.fromRawDtoData(splitAtCommas(rawMap.getMap()));

        MapMatrix<String> mapMatrixPrototype;
        try {
            mapMatrixPrototype = mapMatrixBuilder.validateAndBuild();
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Could not create map " + rawMap.getName(), e);
        }

        MapMatrix<TilePrototype> mapMatrix = mapMatrixPrototype.convertTiles(tileConverter::stringToObject);
        if (!tileConverter.getValidationMessages().isEmpty()) {
            throw new InvalidInputException("Could not convert map tiles:\n" + String.join(",\n", tileConverter.getValidationMessages()));
        }
        return mapMatrix;
    }

    private List<EnemyDto> validateAndLocateEnemies(RawMapDto rawMapDto) throws InvalidInputException {
        List<String> validationMessages = new LinkedList<>();
        List<EnemyDto> result = rawMapDto.getEnemies().stream().map(mapEnemyDto -> {
            try {
                return enemyReader.load(mapEnemyDto.getName()).at(mapEnemyDto.positionX, mapEnemyDto.positionY);
            } catch (InvalidInputException e) {
                validationMessages.add(e.getMessage());
                return null;
            }
        }).collect(toList());

        if (!validationMessages.isEmpty()) {
            throw new InvalidInputException("Error when creating enemies on map " + rawMapDto.getName() + ":\n" + String.join(",\n", validationMessages));
        }
        return result;
    }
}