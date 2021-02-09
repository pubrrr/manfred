package manfred.infrastructureadapter.enemy;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.game.characters.Velocity;
import manfred.game.config.GameConfig;
import manfred.game.enemy.Enemy;
import manfred.game.map.Map;

@AllArgsConstructor
public class EnemyFactory {
    private final EnemyPrototype enemyPrototype;
    private final GameConfig gameConfig;

    public Enemy createOnMap(Map map) {
        return new Enemy(
            enemyPrototype.getName(),
            Velocity.withSpeed(enemyPrototype.getSpeed()),
            map.tileAt(enemyPrototype.getSpawnX(), enemyPrototype.getSpawnY()).getBottomLeftCoordinate(),
            enemyPrototype.getHealthPoints(),
            enemyPrototype.getImage(),
            gameConfig.getPixelBlockSize().times(5),
            gameConfig
        );
    }
}
