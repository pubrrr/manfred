package manfred.data.enemy;

import lombok.Value;

import java.awt.image.BufferedImage;

@Value
public class EnemyDto {
    String name;
    int healthPoints;
    int speed;
    BufferedImage image;
    int spawnX;
    int spawnY;
}