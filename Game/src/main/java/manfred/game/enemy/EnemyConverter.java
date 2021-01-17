package manfred.game.enemy;

import manfred.data.ObjectConverter;
import manfred.data.enemy.EnemyDto;
import manfred.game.config.GameConfig;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter implements ObjectConverter<EnemyDto, Enemy> {

    private final MapColliderProvider mapColliderProvider;
    private final GameConfig gameConfig;

    public EnemyConverter(MapColliderProvider mapColliderProvider, GameConfig gameConfig) {
        this.mapColliderProvider = mapColliderProvider;
        this.gameConfig = gameConfig;
    }

    public Enemy convert(EnemyDto enemyDto) {
        try {
            return new Enemy(
                enemyDto.getName(),
                enemyDto.getSpeed(),
                enemyDto.getSpawnX() * this.gameConfig.getPixelBlockSize(),
                enemyDto.getSpawnY() * this.gameConfig.getPixelBlockSize(),
                enemyDto.getHealthPoints(),
                enemyDto.getImage(),
                mapColliderProvider.provide(),
                gameConfig.getPixelBlockSize() * 5,
                gameConfig
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
