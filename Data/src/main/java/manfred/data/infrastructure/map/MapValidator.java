package manfred.data.infrastructure.map;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.enemy.EnemiesLoader;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.ObjectTileConverter;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.infrastructure.map.validator.Validator;
import manfred.data.infrastructure.person.PersonsLoader;
import manfred.data.persistence.dto.RawMapDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static manfred.data.infrastructure.StringSplitter.splitAtCommas;

@Component
public class MapValidator {

    private final List<Validator> validators;
    private final ObjectTileConverter tileConverter;
    private final EnemiesLoader enemiesLoader;
    private final PersonsLoader personsLoader;

    public MapValidator(@Qualifier("mapValidators") List<Validator> validators, ObjectTileConverter tileConverter, EnemiesLoader enemiesLoader, PersonsLoader personsLoader) {
        this.validators = validators;
        this.tileConverter = tileConverter;
        this.enemiesLoader = enemiesLoader;
        this.personsLoader = personsLoader;
    }

    public MapPrototype validate(RawMapDto rawMap) throws InvalidInputException {
        // TODO validate that map tile image interferes with other images on the map
        MapMatrix<TilePrototype> mapMatrix = buildMapMatrix(rawMap);

        validateMapObjects(rawMap, mapMatrix);

        try {
            return new MapPrototype(
                rawMap.getName(),
                mapMatrix,
                personsLoader.load(rawMap.getPersons()),
                rawMap.getPortals(),
                rawMap.getDoors(),
                enemiesLoader.load(rawMap.getEnemies()),
                rawMap.getMapSource()
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
