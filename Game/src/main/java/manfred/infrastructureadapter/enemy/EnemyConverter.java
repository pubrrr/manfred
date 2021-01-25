package manfred.infrastructureadapter.enemy;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.game.config.GameConfig;
import manfred.game.enemy.Enemy;
import manfred.game.enemy.MapColliderProvider;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter implements ObjectConverter<EnemyPrototype, Enemy> {

    private final MapColliderProvider mapColliderProvider;
    private final GameConfig gameConfig;

    public EnemyConverter(MapColliderProvider mapColliderProvider, GameConfig gameConfig) {
        this.mapColliderProvider = mapColliderProvider;
        this.gameConfig = gameConfig;
    }

    public Enemy convert(EnemyPrototype enemyPrototype) {
        try {
            return new Enemy(
                enemyPrototype.getName(),
                enemyPrototype.getSpeed(),
                enemyPrototype.getSpawnX() * this.gameConfig.getPixelBlockSize(),
                enemyPrototype.getSpawnY() * this.gameConfig.getPixelBlockSize(),
                enemyPrototype.getHealthPoints(),
                enemyPrototype.getImage(),
                mapColliderProvider.provide(),
                gameConfig.getPixelBlockSize() * 5,
                gameConfig
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
