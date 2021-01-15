package manfred.game.enemy;

import lombok.AllArgsConstructor;
import manfred.game.characters.MapCollider;
import manfred.game.config.GameConfig;

import java.awt.image.BufferedImage;

@AllArgsConstructor
public class UnlocatedEnemy {
    private final String name;
    private final int speed;
    private final int healthPoints;
    private final BufferedImage image;
    private final MapCollider mapCollider;
    private final int aggroRadius;
    private final GameConfig gameConfig;

    public Enemy at(int spawnX, int spawnY) {
        return new Enemy(
            this.name,
            this.speed,
            gameConfig.getPixelBlockSize() * spawnX,
            gameConfig.getPixelBlockSize() * spawnY,
            this.healthPoints,
            this.image,
            this.mapCollider,
            this.aggroRadius,
            this.gameConfig
        );
    }
}
