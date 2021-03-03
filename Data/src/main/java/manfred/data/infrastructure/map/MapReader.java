package manfred.data.infrastructure.map;

import manfred.data.InvalidInputException;
import manfred.data.persistence.ObjectReader;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.reader.MapSource;
import manfred.data.persistence.reader.RawMapReader;
import org.springframework.stereotype.Component;

@Component
public class MapReader implements ObjectReader<MapSource, MapPrototype> {

    private final RawMapReader rawMapReader;
    private final MapValidator mapValidator;

    public MapReader(RawMapReader rawMapReader, MapValidator mapValidator) {
        this.rawMapReader = rawMapReader;
        this.mapValidator = mapValidator;
    }

    @Override
    public MapPrototype load(String name) throws InvalidInputException {
        RawMapDto rawMapDto = rawMapReader.load(name);
        return mapValidator.validate(rawMapDto);
    }

    @Override
    public MapPrototype load(MapSource source) throws InvalidInputException {
        RawMapDto rawMapDto = rawMapReader.load(source);
        return mapValidator.validate(rawMapDto);
    }
}
