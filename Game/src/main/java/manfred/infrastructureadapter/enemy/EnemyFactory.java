package manfred.infrastructureadapter.enemy;

import lombok.AllArgsConstructor;
import manfred.data.infrastructure.enemy.EnemyPrototype;
import manfred.data.shared.PositiveInt;
import manfred.game.characters.Velocity;
import manfred.game.characters.sprite.SimpleSprite;
import manfred.game.config.GameConfig;
import manfred.game.enemy.Enemy;
import manfred.game.map.Map;

@AllArgsConstructor
public class EnemyFactory {
    private final EnemyPrototype enemyPrototype;
    private final GameConfig gameConfig;

    public Enemy createOnMap(Map map) {
        PositiveInt.Strict size = gameConfig.getPixelBlockSize().times(2);
        SimpleSprite sprite = new SimpleSprite(size, size, enemyPrototype.getImage());

        return new Enemy(
            enemyPrototype.getName(),
            Velocity.withSpeed(enemyPrototype.getSpeed()),
            map.tileAt(enemyPrototype.getSpawnX(), enemyPrototype.getSpawnY()).getBottomLeftCoordinate(),
            enemyPrototype.getHealthPoints(),
            gameConfig.getPixelBlockSize().times(5),
            gameConfig,
            sprite
        );
    }
}
