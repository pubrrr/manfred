package manfred.data.map.validator;

import lombok.AllArgsConstructor;
import manfred.data.helper.UrlHelper;
import manfred.data.map.MapEnemyDto;
import manfred.data.map.RawMapDto;
import manfred.data.map.matrix.MapMatrix;
import manfred.data.map.tile.TilePrototype;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
public class EnemyValidator extends MapObjectDtoValidator<MapEnemyDto> implements Validator {

    private final UrlHelper urlHelper;

    @Override
    public List<String> validate(RawMapDto rawMapDto, MapMatrix<TilePrototype> mapMatrix) {
        List<MapEnemyDto> enemies = rawMapDto.getEnemies();

        List<String> validationMessages = new LinkedList<>(validateTargetsExist(enemies, urlHelper::getResourceForEnemy));
        validationMessages.addAll(validateTilesAreAccessible(mapMatrix, enemies));

        return validationMessages;
    }

    @Override
    protected Function<Map.Entry<String, Optional<URL>>, String> targetNotExistentErrorMessage() {
        return emptyResourceByTarget -> "Resource for enemy " + emptyResourceByTarget.getKey() + " not found";
    }

    @Override
    protected Function<Map.Entry<String, Boolean>, String> accessibilityErrorMessage() {
        return targetEntry -> "Tile for enemy " + targetEntry.getKey() + " is not accessible";
    }
}
