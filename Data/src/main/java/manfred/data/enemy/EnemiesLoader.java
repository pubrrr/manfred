package manfred.data.enemy;

import lombok.AllArgsConstructor;
import manfred.data.InvalidInputException;
import manfred.data.map.MapEnemyDto;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class EnemiesLoader {

    private final EnemyReader enemyReader;

    public List<LocatedEnemyDto> load(List<MapEnemyDto> enemiesOnMap) throws InvalidInputException {
        List<String> errorMessages = new LinkedList<>();
        List<LocatedEnemyDto> result = enemiesOnMap.stream().map(mapEnemyDto -> {
            try {
                return enemyReader.load(mapEnemyDto.getName()).at(mapEnemyDto.getPositionX(), mapEnemyDto.getPositionY());
            } catch (InvalidInputException e) {
                errorMessages.add(e.getMessage());
                return null;
            }
        }).collect(toList());

        if (!errorMessages.isEmpty()) {
            throw new InvalidInputException(String.join(",\n", errorMessages));
        }
        return result;
    }
}
