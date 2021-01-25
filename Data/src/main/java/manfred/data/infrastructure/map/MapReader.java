package manfred.data.infrastructure.map;

import manfred.data.InvalidInputException;
import manfred.data.persistence.ObjectReader;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.reader.RawMapReader;
import org.springframework.stereotype.Component;

@Component
public class MapReader implements ObjectReader<MapPrototype> {

    private final RawMapReader rawMapReader;
    private final MapValidator mapValidator;

    public MapReader(RawMapReader rawMapReader, MapValidator mapValidator) {
        this.rawMapReader = rawMapReader;
        this.mapValidator = mapValidator;
    }

    public MapPrototype load(String name) throws InvalidInputException {
        RawMapDto rawMapDto = rawMapReader.load(name);
        return mapValidator.validate(rawMapDto);
    }
}
