package manfred.game.enemy;

import manfred.data.InvalidInputException;
import manfred.data.enemy.EnemyDto;
import manfred.game.config.GameConfig;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter {

    private final MapColliderProvider mapColliderProvider;
    private final GameConfig gameConfig;

    public EnemyConverter(MapColliderProvider mapColliderProvider, GameConfig gameConfig) {
        this.mapColliderProvider = mapColliderProvider;
        this.gameConfig = gameConfig;
    }

    public Enemy convert(EnemyDto enemyDto, int spawnX, int spawnY) throws InvalidInputException {
        try {
            return new Enemy(
                enemyDto.getName(),
                enemyDto.getSpeed(),
                gameConfig.getPixelBlockSize() * spawnX,
                gameConfig.getPixelBlockSize() * spawnY,
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
