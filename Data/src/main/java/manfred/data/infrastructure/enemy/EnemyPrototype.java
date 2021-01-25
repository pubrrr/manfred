package manfred.data.infrastructure.enemy;

import lombok.Value;

import java.awt.image.BufferedImage;

@Value
public class EnemyPrototype {
    String name;
    int healthPoints;
    int speed;
    BufferedImage image;
    int spawnX;
    int spawnY;
}
