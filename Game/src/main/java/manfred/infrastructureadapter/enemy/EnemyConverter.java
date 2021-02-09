package manfred.infrastructureadapter.enemy;

import manfred.data.infrastructure.ObjectConverter;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.game.config.GameConfig;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter implements ObjectConverter<EnemyPrototype, EnemyFactory> {

    private final GameConfig gameConfig;

    public EnemyConverter(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    @Override
    public EnemyFactory convert(EnemyPrototype enemyPrototype) {
        return new EnemyFactory(enemyPrototype, gameConfig);
    }
}
