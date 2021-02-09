package manfred.data.infrastructure.enemy;

import lombok.Value;
import manfred.data.shared.PositiveInt;

import java.awt.image.BufferedImage;

@Value
public class EnemyPrototype {
    String name;
    PositiveInt healthPoints;
    PositiveInt speed;
    BufferedImage image;
    PositiveInt spawnX;
    PositiveInt spawnY;
}
