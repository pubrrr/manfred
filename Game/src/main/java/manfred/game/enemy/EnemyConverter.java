package manfred.game.enemy;

import manfred.data.InvalidInputException;
import manfred.data.ObjectConverter;
import manfred.data.enemy.EnemyDto;
import manfred.game.config.GameConfig;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter implements ObjectConverter<EnemyDto, UnlocatedEnemy> {

    private final MapColliderProvider mapColliderProvider;
    private final GameConfig gameConfig;

    public EnemyConverter(MapColliderProvider mapColliderProvider, GameConfig gameConfig) {
        this.mapColliderProvider = mapColliderProvider;
        this.gameConfig = gameConfig;
    }

    public UnlocatedEnemy convert(EnemyDto enemyDto) throws InvalidInputException {
        try {
            return new UnlocatedEnemy(
                enemyDto.getName(),
                enemyDto.getSpeed(),
                enemyDto.getHealthPoints(),
                enemyDto.getImage(),
                mapColliderProvider.provide(),
                gameConfig.getPixelBlockSize() * 5,
                gameConfig
            );
        } catch (Exception e) {
            throw new InvalidInputException(e.getMessage());
        }
    }
}
