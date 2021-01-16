package manfred.data.map.tile;

import manfred.data.InvalidInputException;
import manfred.data.map.matrix.MapMatrix;

import java.util.List;

import static manfred.data.helper.StringSplitter.splitAtCommas;

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
