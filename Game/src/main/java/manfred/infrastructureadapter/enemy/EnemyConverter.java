package manfred.infrastructureadapter.enemy;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.game.config.GameConfig;
import manfred.game.enemy.Enemy;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter implements ObjectConverter<EnemyPrototype, Enemy> {

    private final GameConfig gameConfig;

    public EnemyConverter(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public Enemy convert(EnemyPrototype enemyPrototype) {
        try {
            return new Enemy(
                enemyPrototype.getName(),
                enemyPrototype.getSpeed(),
                enemyPrototype.getSpawnX().value() * this.gameConfig.getPixelBlockSize(),
                enemyPrototype.getSpawnY().value() * this.gameConfig.getPixelBlockSize(),
                enemyPrototype.getHealthPoints(),
                enemyPrototype.getImage(),
                gameConfig.getPixelBlockSize() * 5,
                gameConfig
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
