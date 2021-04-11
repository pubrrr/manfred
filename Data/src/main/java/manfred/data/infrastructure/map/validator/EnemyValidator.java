package manfred.data.infrastructure.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.map.matrix.MapMatrix;
import manfred.data.infrastructure.map.tile.TilePrototype;
import manfred.data.persistence.dto.MapEnemyDto;
import manfred.data.persistence.dto.RawMapDto;
import manfred.data.persistence.reader.UrlHelper;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class EnemyValidator extends MapObjectDtoValidator<MapEnemyDto> implements Validator {

    private final UrlHelper urlHelper;

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<MapEnemyDto> enemies = rawMapDto.getEnemies();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(enemies, urlHelper::getFileForEnemy));
        validationMessages.addAll(validateTilesAreAccessible(mapMatrix, enemies));

        return validationMessages;
    }

    @Override
    protected Function<Map.Entry<String, File>, String> targetNotExistentErrorMessage() {
        return emptyResourceByTarget -> "Resource for enemy " + emptyResourceByTarget.getKey() + " not found";
    }

    @Override
    protected Function<Map.Entry<String, Boolean>, String> accessibilityErrorMessage() {
        return targetEntry -> "Tile for enemy " + targetEntry.getKey() + " is not accessible";
    }
}
