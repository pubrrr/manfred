package manfred.data.map;

import manfred.data.InvalidInputException;
import manfred.data.enemy.EnemiesLoader;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TileConverter;
import manfred.data.map.tile.TilePrototype;
import manfred.data.map.validator.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static manfred.data.helper.StringSplitter.splitAtCommas;

@Component
public class MapDtoValidator {

    private final List<Validator> validators;
    private final TileConverter tileConverter;
    private final EnemiesLoader enemiesLoader;

    public MapDtoValidator(@Qualifier("mapValidators") List<Validator> validators, TileConverter tileConverter, EnemiesLoader enemiesLoader) {
        this.validators = validators;
        this.tileConverter = tileConverter;
        this.enemiesLoader = enemiesLoader;
    }

    public ValidatedMapDto validate(RawMapDto rawMap) throws InvalidInputException {
        // TODO validate that map tile image interferes with other images on the map
        MapMatrix<TilePrototype> mapMatrix = buildMapMatrix(rawMap);

        validateMapObjects(rawMap, mapMatrix);

        try {
            return new ValidatedMapDto(
                rawMap.getName(),
                mapMatrix,
                rawMap.getPersons(),
                rawMap.getPortals(),
                rawMap.getDoors(),
                enemiesLoader.load(rawMap.getEnemies())
            );
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Error when creating objects for map " + rawMap.getName(), e);
        }
    }

    private void validateMapObjects(RawMapDto rawMap, MapMatrix<TilePrototype> mapMatrix) throws InvalidInputException {
        List<String> validationMessages = this.validators.stream()
            .map(validator -> validator.validate(rawMap, mapMatrix))
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
}
