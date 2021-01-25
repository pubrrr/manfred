package manfred.data.infrastructure.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.persistence.ObjectReader;
import manfred.data.persistence.reader.RawMapTileDtoReader;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapTileReader implements ObjectReader<ValidatedMapTileDto> {

    private final RawMapTileDtoReader rawMapTileDtoReader;
    private final MapTileDtoValidator mapTileDtoValidator;

    @Override
    public ValidatedMapTileDto load(String name) throws InvalidInputException {
        return mapTileDtoValidator.validate(rawMapTileDtoReader.load(name));
    }
}
