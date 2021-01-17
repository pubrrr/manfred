package manfred.data.map;

import manfred.data.InvalidInputException;
import manfred.data.ObjectReader;
import org.springframework.stereotype.Component;

@Component
public class MapReader implements ObjectReader<ValidatedMapDto> {

    private final RawMapReader rawMapReader;
    private final MapDtoValidator mapDtoValidator;

    public MapReader(RawMapReader rawMapReader, MapDtoValidator mapDtoValidator) {
        this.rawMapReader = rawMapReader;
        this.mapDtoValidator = mapDtoValidator;
    }

    public ValidatedMapDto load(String name) throws InvalidInputException {
        RawMapDto rawMapDto = rawMapReader.load(name);
        return mapDtoValidator.validate(rawMapDto);
    }
}
