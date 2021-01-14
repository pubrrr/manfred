package manfred.data.map;

import manfred.data.InvalidInputException;

public class MapReader {

    private final RawMapReader rawMapReader;
    private final MapDtoValidator mapDtoValidator;

    public MapReader(RawMapReader rawMapReader, MapDtoValidator mapDtoValidator) {
        this.rawMapReader = rawMapReader;
        this.mapDtoValidator = mapDtoValidator;
    }

    public ValidatedMapDto read(String name) throws InvalidInputException {
        RawMapDto rawMapDto = rawMapReader.load(name);
        return mapDtoValidator.validate(rawMapDto);
    }
}
