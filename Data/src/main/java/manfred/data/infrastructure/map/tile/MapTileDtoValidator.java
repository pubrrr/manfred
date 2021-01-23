package manfred.data.infrastructure.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.persistence.dto.RawMapTileDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static manfred.data.infrastructure.StringSplitter.splitAtCommas;

@Component
public class MapTileDtoValidator {

    public ValidatedMapTileDto validate(RawMapTileDto rawMapTile) throws InvalidInputException {
        try {
            List<List<String>> rawStructure = splitAtCommas(rawMapTile.getStructure());
            return new ValidatedMapTileDto(
                rawMapTile.getName(),
                MapMatrix.fromRawDtoData(rawStructure).validateAndBuild(),
                rawMapTile.getImage()
            );
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Validation of map tile " + rawMapTile.getName() + " failed", e);
        }
    }
}
