package manfred.data.infrastructure.map.tile;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.infrastructure.map.MapPrototype;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.persistence.dto.RawMapTileDto;
import org.springframework.stereotype.Component;

import java.util.List;

import static manfred.data.infrastructure.StringSplitter.splitAtCommas;

@Component
@AllArgsConstructor
public class MapTileDtoValidator {

    private final AccessibilityTileConverter tileConverter;

    public ValidatedMapTileDto validate(RawMapTileDto rawMapTile) throws InvalidInputException {
        try {
            List<List<String>> rawStructure = splitAtCommas(rawMapTile.getStructure());
            MapMatrix<TilePrototype> structure = MapMatrix.fromRawDtoData(rawStructure)
                .validateAndBuild()
                .convertTiles(tileConverter::stringToObject);

            return new ValidatedMapTileDto(
                rawMapTile.getName(),
                new MapTileStructurePrototype(structure),
                rawMapTile.getImage(),
                rawMapTile.getImageData()
            );
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Validation of map tile " + rawMapTile.getName() + " failed", e);
        }
    }
}
