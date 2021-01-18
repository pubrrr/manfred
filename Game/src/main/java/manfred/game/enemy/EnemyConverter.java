package manfred.game.enemy;

import manfred.data.ObjectConverter;
import manfred.data.enemy.LocatedEnemyDto;
import manfred.game.config.GameConfig;
import org.springframework.stereotype.Component;

@Component
public class EnemyConverter implements ObjectConverter<LocatedEnemyDto, Enemy> {

    private final MapColliderProvider mapColliderProvider;
    private final GameConfig gameConfig;

    public EnemyConverter(MapColliderProvider mapColliderProvider, GameConfig gameConfig) {
        this.mapColliderProvider = mapColliderProvider;
        this.gameConfig = gameConfig;
    }

    public Enemy convert(LocatedEnemyDto locatedEnemyDto) {
        try {
            return new Enemy(
                locatedEnemyDto.getName(),
                locatedEnemyDto.getSpeed(),
                locatedEnemyDto.getSpawnX() * this.gameConfig.getPixelBlockSize(),
                locatedEnemyDto.getSpawnY() * this.gameConfig.getPixelBlockSize(),
                locatedEnemyDto.getHealthPoints(),
                locatedEnemyDto.getImage(),
                mapColliderProvider.provide(),
                gameConfig.getPixelBlockSize() * 5,
                gameConfig
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
